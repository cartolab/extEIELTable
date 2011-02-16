package es.udc.cartolab.gvsig.eielforms;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.udc.cartolab.gvsig.eielforms.domain.generator.DomainGenerator2;
import es.udc.cartolab.gvsig.eielforms.group.ComplexGroup;
import es.udc.cartolab.gvsig.eielforms.group.Group;
import es.udc.cartolab.gvsig.eielforms.group.SimpleGroup;
import es.udc.cartolab.gvsig.eielforms.group.SubGroup;
import es.udc.cartolab.gvsig.eielforms.gui.TableFrame;
import es.udc.cartolab.gvsig.eielforms.structure.ConfigurationValues;
import es.udc.cartolab.gvsig.eielforms.structure.Dependency;
import es.udc.cartolab.gvsig.eielforms.structure.SingleField;
import es.udc.cartolab.gvsig.eielforms.structure.XmlParsingResults;




public abstract class Parser {


	public static TableFrame parseXmlDoc(Document doc, Object[][] data) {
		doc.getDocumentElement().normalize();
		NodeList children = doc.getChildNodes();
		XmlParsingResults results = new XmlParsingResults();
		parseForm(children.item(0), results);

		TableFrame newContentPane = null;

		//Create and set up the content pane.
		if (results.getDependencies().size() > 0) {
			newContentPane = new TableFrame(results.getDependencies().get(0).getFields(), data);
			newContentPane.setOpaque(true); //content panes must be opaque
		} else if (results.getGroups().size() > 0) {
			newContentPane = new TableFrame(results.getGroups().get(0).getSubGroup().getFields(), data);
			newContentPane.setOpaque(true); //content panes must be opaque
		}

		return newContentPane;

	}

	public static XmlParsingResults parseForm(Node node, XmlParsingResults results) {

		int i;

		if (node.getNodeType() == Node.ELEMENT_NODE) {
			if (node.getNodeName().equals("ConfigurationValues")) {
				results.setConfigurationValues(parseConfigValues(node));
			} else if (node.getNodeName().equals("Dependency")) {
				results.addDependency(parseDependency(node));
			} else if (node.getNodeName().equals("Group")) {
				results.addGroups(parseGroup(node));
			} else if (node.hasChildNodes()) {
				for (i=0; i<node.getChildNodes().getLength(); i++) {
					parseForm(node.getChildNodes().item(i), results);
				}
			}
		} else if (node.hasChildNodes()) {
			for (i=0; i<node.getChildNodes().getLength(); i++) {
				parseForm(node.getChildNodes().item(i), results);
			}
		}

		return results;
	}

	public static SingleField parseField(Node node) {

		SingleField field = new SingleField();
		Node auxN;
		int i;

		if (node.hasChildNodes()) {
			NodeList auxL = node.getChildNodes();
			for (i=0; i<auxL.getLength(); i++) {
				auxN = auxL.item(i);
				if ((auxN.getNodeName().equals("Name")) && (auxN.hasChildNodes())) {
					field.setName(((Node) auxN.getChildNodes().item(0)).getNodeValue());
				} else if ((auxN.getNodeName().equals("Label")) && (auxN.hasChildNodes())) {
					field.setLabel(((Node) auxN.getChildNodes().item(0)).getNodeValue());
				} else if ((auxN.getNodeName().equals("Domain")) && (auxN.hasChildNodes())) {
					field.setDomain(DomainGenerator2.getDomain(((Node) auxN.getChildNodes().item(0)).getNodeValue()));
				} else if ((auxN.getNodeName().equals("DefaultValue")) && (auxN.hasChildNodes())) {
					if ((((Node) auxN.getChildNodes().item(0)).getNodeName().equals("ConstantValue")) && (((Node) auxN.getChildNodes().item(0)).hasChildNodes())) {
						field.setDefaultValue(((Node) ((Node) auxN.getChildNodes().item(0)).getChildNodes().item(0)).getNodeValue());
					}
				} else if ((auxN.getNodeName().equals("IsKey")) && (auxN.hasChildNodes())) {
					field.setIsKey(((Node) auxN.getChildNodes().item(0)).getNodeValue().toLowerCase().equals("true"));
				} else if ((auxN.getNodeName().equals("Editable")) && (auxN.hasChildNodes())) {
					field.setEditable(((Node) auxN.getChildNodes().item(0)).getNodeValue().toLowerCase().equals("true"));
				} else if ((auxN.getNodeName().equals("IsOrden")) && (auxN.hasChildNodes())) {
					field.setIsOrden(((Node) auxN.getChildNodes().item(0)).getNodeValue().toLowerCase().equals("true"));
				} else if ((auxN.getNodeName().equals("Required")) && (auxN.hasChildNodes())) {
					field.setRequired(((Node) auxN.getChildNodes().item(0)).getNodeValue().toLowerCase().equals("true"));
				}
			}
		}

		return field;

	}

	public static Group parseGroup(Node node) {
		Group group = new Group();

		if (node.hasAttributes()) {
			group.setName(node.getAttributes().getNamedItem("GroupName").getNodeValue());
		}

		Node childN;
		int i;

		NodeList auxL = ((Element) node).getElementsByTagName("Layout");

		if (auxL.getLength() > 0) {
			group.setLayout(((Node) auxL.item(0)).getChildNodes().item(0).getNodeValue());
		}

		auxL = node.getChildNodes();

		for (i=0; i<auxL.getLength(); i++) {
			childN = auxL.item(i);
			if ((childN.getNodeName().equals("ContainedSimpleGroup")) && (childN.hasChildNodes())) {
				group.setSubGroup(parseSimpleGroup(childN));
			} else if ((childN.getNodeName().equals("ContainedComplexGroup")) && (childN.hasChildNodes())) {
				group.setSubGroup(parseComplexGroup(childN));
			}
		}

		return group;
	}

	public static SimpleGroup parseSimpleGroup(Node node) {

		SimpleGroup group = new SimpleGroup();

		if (node.hasAttributes()) {
			group.setName(node.getAttributes().getNamedItem("GroupName").getNodeValue());
		}

		Element auxN;

		NodeList auxL = ((Element) node).getElementsByTagName("Layout");

		if (auxL.getLength() > 0) {
			group.setLayout(((Node) auxL.item(0)).getChildNodes().item(0).getNodeValue());
		}

		auxL = ((Element) node).getElementsByTagName("Fields");

		if (auxL.getLength() > 0) {
			auxN = (Element) auxL.item(0);
			if (auxN.hasChildNodes()) {
				group.setFields(parseFields(auxN));
			}
		}

		return group;

	}

	public static ArrayList<SingleField> parseFields(Node node) {
		int i;
		Node childN;
		ArrayList<SingleField> fields = new ArrayList<SingleField>();

		if (node.hasChildNodes()) {
			for (i=0; i<node.getChildNodes().getLength(); i++) {
				childN = node.getChildNodes().item(i);
				if ((childN.getNodeName().equals("Field")) && (childN.hasChildNodes())) {
					fields.add(parseField(childN));
				}
			}
		}

		return fields;
	}

	public static ComplexGroup parseComplexGroup(Node node) {

		ComplexGroup group = new ComplexGroup();

		Node auxN, childN;
		int i;

		NodeList auxL = ((Element) node).getElementsByTagName("Layout");

		if (auxL.getLength() > 0) {
			group.setLayout(((Node) auxL.item(0)).getChildNodes().item(0).getNodeValue());
		}

		auxL = node.getChildNodes();

		for (i=0; i<auxL.getLength(); i++) {
			auxN = auxL.item(i);
			if ((auxN.getNodeName().equals("ContainedSimpleGroup")) && (auxN.hasChildNodes())) {
				group.addSubGroup(parseSimpleGroup(auxN));
			} else if ((auxN.getNodeName().equals("ContainedComplexGroup")) && (auxN.hasChildNodes())) {
				group.addSubGroup(parseComplexGroup(auxN));
			} else if ((auxN.getNodeName().equals("Fields")) && (auxN.hasChildNodes())) {
				for (i=0; i<auxN.getChildNodes().getLength(); i++) {
					childN = auxN.getChildNodes().item(i);
					if ((childN.getNodeName().equals("Field")) && (childN.hasChildNodes())) {
						group.addField(parseField(childN));
					}
				}
			}
		}

		return group;

	}

	public static Dependency parseDependency(Node node) {

		Dependency dependency = new Dependency();
		if (node.hasAttributes()) {
			dependency.setTable(node.getAttributes().getNamedItem("Table").getNodeValue());
			dependency.setDataBase(node.getAttributes().getNamedItem("DataBase").getNodeValue());
		}

		Element auxN;

		NodeList auxL = ((Element) node).getElementsByTagName("Layout");

		if (auxL.getLength() > 0) {
			dependency.setLayout(((Node) auxL.item(0)).getChildNodes().item(0).getNodeValue());
		}

		auxL = ((Element) node).getElementsByTagName("Fields");

		if (auxL.getLength() > 0) {
			auxN = (Element) auxL.item(0);
			if (auxN.hasChildNodes()) {
				dependency.setFields(parseFields(auxN));
			}
		}

		return dependency;

	}

	public static ConfigurationValues parseConfigValues(Node node) {

		ConfigurationValues config = new ConfigurationValues();
		Element auxN;
		NodeList auxL = ((Element) node).getElementsByTagName("DataBase");

		if (auxL.getLength() > 0) {
			auxN = (Element) auxL.item(0);
			if (auxN.hasChildNodes()) {
				config.setDataBase(((Node) auxN.getChildNodes().item(0)).getNodeValue());
			}
		} else {
			System.out.println("DataBase is not defined inside ConfigurationValues");
		}


		auxL = ((Element) node).getElementsByTagName("Table");
		if (auxL.getLength() > 0) {
			auxN = (Element) auxL.item(0);
			if (auxN.hasChildNodes()) {
				config.setTable(((Node) auxN.getChildNodes().item(0)).getNodeValue());
			}
		} else {
			System.out.println("Table is not defined inside ConfigurationValues");
		}


		auxL = ((Element) node).getElementsByTagName("Layer");
		if (auxL.getLength() > 0) {
			auxN = (Element) auxL.item(0);
			if (auxN.hasChildNodes()) {
				config.setLayer(((Node) auxN.getChildNodes().item(0)).getNodeValue());
			}
		} else {
			System.out.println("Layer is not defined inside ConfigurationValues");
		}

		return config;
	}

	public static void printConfig(ConfigurationValues config) {

		System.out.println("ConfigurationValues:");
		System.out.println(config.getDataBase());
		System.out.println(config.getTable());
		System.out.println(config.getLayer());

	}

	public static void printDependency(Dependency dependency) {

		System.out.println("DataBase " + dependency.getDataBase() + " | Layout " + dependency.getLayout() + " | Table " + dependency.getTable());
		int i;
		System.out.println("Campos:");
		for (i=0; i<dependency.getFields().size(); i++) {
			System.out.println(dependency.getFields().get(i).getName());
			System.out.println(dependency.getFields().get(i).getLabel());
			System.out.println(dependency.getFields().get(i).getDomain().getName());
			System.out.println(dependency.getFields().get(i).getDomain().toString());
			System.out.println(dependency.getFields().get(i).getIsKey());
			System.out.println(dependency.getFields().get(i).getEditable());
			System.out.println(dependency.getFields().get(i).getRequired());
			System.out.println(dependency.getFields().get(i).getIsOrden());
			System.out.println(dependency.getFields().get(i).getDefaultValue());
		}

	}

	public static void printSimpleGroup(SimpleGroup group) {

		System.out.println("Grupo " + group.getName() + " | Layout " + group.getLayout());
		int i;
		System.out.println("Campos:");
		for (i=0; i<group.getFields().size(); i++) {
			System.out.println(group.getFields().get(i).getName());
			System.out.println(group.getFields().get(i).getLabel());
			System.out.println(group.getFields().get(i).getDomain().getName());
			System.out.println(group.getFields().get(i).getDomain().toString());
			System.out.println(group.getFields().get(i).getIsKey());
			System.out.println(group.getFields().get(i).getEditable());
			System.out.println(group.getFields().get(i).getRequired());
			System.out.println(group.getFields().get(i).getIsOrden());
			System.out.println(group.getFields().get(i).getDefaultValue());
		}

	}

	public static void printComplexGroup(ComplexGroup group) {

		System.out.println("Grupo " + group.getName() + " | Layout " + group.getLayout());
		int i;
		ArrayList<SubGroup> groups = group.getSubGroups();
		SubGroup subGroup;

		System.out.println("SubGrupos:");
		if (!groups.isEmpty()) {
			for (i=0; i<groups.size(); i++) {
				subGroup = groups.get(i);
				if (subGroup instanceof SimpleGroup) {
					printSimpleGroup((SimpleGroup) subGroup);
				} else {
					printComplexGroup((ComplexGroup) subGroup);
				}
			}
		}

	}

	/*public static void printResults(FormXmlParsingResults results) {

		int i;
		printConfig(results.getConfigurationValues());

		ArrayList<Group> groups = results.getGroups();
		SubGroup subGroup;

		if (!groups.isEmpty()) {
			for (i=0; i<groups.size(); i++) {
				System.out.println("Grupo " + groups.get(i).getName() + " | Layout " + groups.get(i).getLayout());
				subGroup = groups.get(i).getSubGroup();
				if (subGroup instanceof SimpleGroup) {
					printSimpleGroup((SimpleGroup) subGroup);
				} else {
					printComplexGroup((ComplexGroup) subGroup);
				}
			}
		}

		ArrayList<Dependency> dependencies = results.getDependencies();

		if (!dependencies.isEmpty()) {
			for (i=0; i<dependencies.size(); i++) {
				printDependency(dependencies.get(i));
			}
		}

	}*/

}
