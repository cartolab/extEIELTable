/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
 *
 * This file is part of extEIELForms
 *
 * extEIELForms is based on the forms application of GisEIEL <http://giseiel.forge.osor.eu/>
 * devoloped by Laboratorio de Bases de Datos (Universidade da Coruña)
 *
 * extEIELForms is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * extEIELForms is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with extEIELForms.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package es.udc.cartolab.gvsig.eielforms.formgenerator;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import es.udc.cartolab.gvsig.eielforms.dependency.Dependency;
import es.udc.cartolab.gvsig.eielforms.dependency.DependencyGenerator;
import es.udc.cartolab.gvsig.eielforms.forms.FormController;
import es.udc.cartolab.gvsig.eielforms.groups.FieldGroup;
import es.udc.cartolab.gvsig.eielforms.groups.GroupGenerator;
import es.udc.cartolab.gvsig.eielforms.nucleosrelation.NucleosRelation;
import es.udc.cartolab.gvsig.eielforms.nucleosrelation.NucleosRelationGenerator;
import es.udc.cartolab.gvsig.eielforms.subforms.SubForm;
import es.udc.cartolab.gvsig.eielforms.subforms.SubFormGenerator;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormGenerator
{
	private FormReader formDAO;
	private DOMParser domParser;
	private ArrayList dependencies;
	private ArrayList groups;
	private ArrayList subform;
	private String name;
	private String database;
	private String table;
	private String layer;
	private String title;
	private NucleosRelation nucleosRelation;
	private FormController formController;
	private DependencyGenerator dependencyGenerator;
	private GroupGenerator groupGenerator;
	private SubFormGenerator subformGenerator;
	private NucleosRelationGenerator nucleosRelationGenerator;
	private String aplicationDbName;
	private String xml;
	private boolean pollButton;
	private String pollTable;


	public FormGenerator(FormReader reader)
	{
		this.formDAO = reader;
		this.domParser = new DOMParser();
		this.dependencyGenerator = new DependencyGenerator();
		this.groupGenerator = new GroupGenerator();
		this.subformGenerator = new SubFormGenerator();
		this.nucleosRelationGenerator = new NucleosRelationGenerator();
		this.pollButton = false;
		this.pollTable = "";
	}

	public FormGenerator() {

		this(new FormDBReader());

	}

	public FormController createFormController(String formControllerName) throws FormException
	{
		String XMLDefinition = "";

		this.formController = null;
		try
		{
			XMLDefinition = this.formDAO.getFormDefinition(formControllerName);

			if (XMLDefinition != null) {
				this.formController = new FormController("", "", "", "", "", "");
				StringReader sr = new StringReader(XMLDefinition);

				InputSource is = new InputSource(sr);

				System.out.println("FormGenerator.createFormController:InputSource" + is.toString());
				this.domParser.parse(is);

				Node rootNode = this.domParser.getDocument();
				System.out.println("FormGenerator.createFormController:rootNode" + rootNode.toString());
				processFormController(rootNode);

				this.formController = new FormController(this.layer, this.database, this.table, "gridbaglayout", this.name, this.title);

				for (int i = 0; i < this.dependencies.size(); ++i) {
					this.formController.addDependency((Dependency)this.dependencies.get(i));
				}

				for (int i = 0; i < this.groups.size(); ++i) {
					this.formController.addGroup((FieldGroup)this.groups.get(i));
				}

				if (this.subform.size() != 0)
				{
					this.formController.addSubformsButton(this.subform);
				}

				for (int i = 0; i < this.subform.size(); ++i) {
					SubForm subformulario = (SubForm)this.subform.get(i);

					this.formController.addSubForm(subformulario);
				}
				if (this.nucleosRelation != null) {
					this.formController.addNucleosRelationButton(this.nucleosRelation);
				}

				if (this.pollButton) {
					this.formController.addPollButton(this.pollTable);
				}
			}
			else {
				System.out.println("El formulario " + formControllerName + " no ha sido encontrado !!!");
			}

			return this.formController;
		}
		catch (SAXException e) {
			System.out.println("(1)Archivo XML mal formado.");
			throw new FormException(e);
		} catch (IOException e) {
			System.out.println("Error de lectura");
			throw new FormException(e);
		}catch (FormException e) {
			System.out.println("Error al generar el formulario -> " + formControllerName);
			throw e;
		}

	}

	private void processFormController(Node rootNode)
	{
		this.dependencies = new ArrayList();
		this.groups = new ArrayList();
		this.subform = new ArrayList();
		Node formNode = rootNode.getFirstChild();
		this.name = formNode.getAttributes().getNamedItem("name").getNodeValue();
		if (formNode.getAttributes().getNamedItem("title") != null) {
			this.title = formNode.getAttributes().getNamedItem("title").getNodeValue();
		} else {
			this.title = this.name;
		}

		Node attributes = formNode.getFirstChild();

		while (attributes != null) {
			if (attributes.getNodeName().compareTo("ConfigurationValues") == 0) {
				processConfigurationValues(attributes);
			} else {
				if (attributes.getNodeName().compareTo("Dependencies") == 0) {
					Node nodo_dependencias = attributes.getFirstChild();
					while (true) { if (nodo_dependencias == null) {
						break;
					}
					if (nodo_dependencias.getNodeName().compareTo("Dependency") == 0) {
						this.dependencies.add(this.dependencyGenerator.processDependency(nodo_dependencias));
					}
					nodo_dependencias = nodo_dependencias.getNextSibling();
					}
				}
				if (attributes.getNodeName().compareTo("Group") == 0) {
					this.groups.add(this.groupGenerator.processGroup(attributes));
				}
				else if (attributes.getNodeName().compareTo("Subform") == 0) {
					this.subform.add(this.subformGenerator.processSubForm(attributes));
				}
				else if (attributes.getNodeName().compareTo("PollButton") == 0) {
					this.pollButton = true;
					this.pollTable = attributes.getAttributes().getNamedItem("Table").getNodeValue();
				}
				else if (attributes.getNodeName().compareTo("NucleosRelation") == 0) {
					this.nucleosRelation = this.nucleosRelationGenerator.processNucleosRelation(attributes);
				}

			}

			attributes = attributes.getNextSibling();
		}

		formNode = null;
		attributes = null;
	}

	private void processConfigurationValues(Node configurationValuesNode)
	{
		Node attributes = configurationValuesNode.getFirstChild();
		while (attributes != null) {
			if (attributes.getNodeName().compareTo("DataBase") == 0) {
				DBSession dbs = DBSession.getCurrentSession();
				if (dbs != null) {
					this.database = dbs.getSchema();
				} else {
					this.database = attributes.getFirstChild().getNodeValue();
				}
			}
			else if (attributes.getNodeName().compareTo("Table") == 0) {
				this.table = attributes.getFirstChild().getNodeValue();
			}
			else if (attributes.getNodeName().compareTo("Layer") == 0) {
				this.layer = attributes.getFirstChild().getNodeValue();
			}

			attributes = attributes.getNextSibling();
		}
	}
}
