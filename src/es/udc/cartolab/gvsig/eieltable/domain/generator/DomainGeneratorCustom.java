/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
 *
 * This file is part of extEIELTable
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

package es.udc.cartolab.gvsig.eieltable.domain.generator;

import java.io.StringReader;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import es.udc.cartolab.gvsig.eieltable.domain.restriction.DecimalSizeRestrictionCustom;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.FieldSizeEqualRestrictionCustom;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.FieldSizeGreaterRestrictionCustom;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.FieldSizeLessRestrictionCustom;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.GreaterThanRestrictionCustom;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.LessThanRestrictionCustom;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.NumericFieldRestrictionCustom;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.RestrictionCustom;
import es.udc.cartolab.gvsig.eieltable.structure.domain.BasicDomain;
import es.udc.cartolab.gvsig.eieltable.structure.domain.Domain;
import es.udc.cartolab.gvsig.eieltable.structure.domain.UserDomain;



/**
 * Domain Generator.
 * 
 * Class used for retrieving domain definitions and
 * parsing them for obtaining Domain objects.
 * 
 * @see Domain
 * @see DomainDBReaderCustom
 * @see DomainFileReaderCustom
 */
public abstract class DomainGeneratorCustom
{
	/**
	 * Domain database reader used for retrieving domain definitions.
	 */
	private static DomainReaderCustom domainDAO = new DomainDBReaderCustom();

	/**
	 * Domain parser used for parsing domain xml definitions and creating Domain objects.
	 */
	private static DOMParser domParser = new DOMParser();

	/**
	 * Domain database reader used for retrieving domain definitions.
	 */
	private static DomainCacheCustom domainCache = new DomainCacheCustom();

	/**
	 * Domain Object Retriever.
	 * 
	 * Method for retrieving domain definitions and
	 * parsing them for obtaining Domain objects.
	 * 
	 * @param domainName name of the domain we want to retrieve.
	 * 
	 * @returns A Domain object as the given domain specifies.
	 */
	public static Domain getDomain(String domainName)
	{
		Domain domain = null;
		String XMLDefinition = "";

		domain = domainCache.getDomain(domainName);
		if (domain == null) {
			try {

				XMLDefinition = domainDAO.getDomainDefinition(domainName);

				if (XMLDefinition != null) {
					StringReader sr = new StringReader(XMLDefinition);
					InputSource is = new InputSource(sr);

					domParser.parse(is);
					Node nodoRaiz = domParser.getDocument();

					domain = processDomain(nodoRaiz);
					domainCache.addDomain(domainName, domain);
				} else {
					System.out.println("El dominio " + domainName + " no ha sido encontrado !!!");
					System.out.println("Crearemos un domain basico tipo String por defecto");
					domain = new BasicDomain("String default", "String");
				}
			}
			catch (Exception e) {
				System.out.println("Algun problema procesando el dominio " + domainName + "!!!");
				e.printStackTrace();
			}
		} else {
			System.out.println("Recuperado el dominio " + domain.getName() + " de la cache");
		}

		return domain;
	}

	/**
	 * Domain Node Processor.
	 * 
	 * Method for parsing a domain xml definition (as a Node
	 * object) and obtaining a Domain object.
	 * 
	 * @param rootNode node of the domain xml definition we want to retrieve.
	 * 
	 * @returns A Domain object as the given root Node specifies.
	 */
	private static Domain processDomain(Node rootNode)
	{
		Node domainNode = rootNode.getFirstChild();
		Domain domain = null;

		if (domainNode.getNodeName().compareTo("domain") == 0) {
			String name = domainNode.getAttributes().getNamedItem("name").getNodeValue();
			Node descriptionNode = domainNode.getFirstChild();
			while (descriptionNode != null) {
				if (descriptionNode.getNodeName().compareTo("UserDomain") == 0) {
					domain = processUserDomain(name, descriptionNode);
				}
				else if (descriptionNode.getNodeName().compareTo("BasicDomain") == 0) {
					domain = processBasicDomain(name, descriptionNode);
				}

				descriptionNode = descriptionNode.getNextSibling();
			}
		}

		return domain;
	}

	/**
	 * User Domain Node Processor.
	 * 
	 * Method for parsing a user domain xml definition (as a Node
	 * object) and obtaining a Domain object.
	 * 
	 * @param name the name of the Domain as defined by its root Node.
	 * @param userDomainNode node of the user domain xml definition we want to retrieve.
	 * 
	 * @returns A User Domain object as the given user domain Node specifies.
	 */
	private static UserDomain processUserDomain(String name, Node userDomainNode)
	{
		String esquema = "";
		String table = "";
		UserDomain domain = null;

		Node attributes = userDomainNode.getFirstChild();
		while (attributes != null) {
			if (attributes.getNodeName().compareTo("DataBase") == 0) {
				esquema = attributes.getFirstChild().getNodeValue();
			}
			else if (attributes.getNodeName().compareTo("Table") == 0) {
				table = attributes.getFirstChild().getNodeValue();
			}

			attributes = attributes.getNextSibling();
		}

		//TODO los dominios que no sean basicos
		try
		{
			domain = domainDAO.getDomain(name, esquema, table);
		}
		catch (DomainException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ha ocurrido un error al generar el dominio " + name);
		}

		return domain;
	}

	/**
	 * Basic Domain Node Processor.
	 * 
	 * Method for parsing a basic domain xml definition (as a Node
	 * object) and obtaining a Domain object.
	 * 
	 * @param name the name of the Domain as defined by its root Node.
	 * @param basicDomainNode node of the basic domain xml definition we want to retrieve.
	 * 
	 * @returns A Basic Domain object as the given basic domain Node specifies.
	 */
	private static BasicDomain processBasicDomain(String name, Node basicDomainNode)
	{
		String basicType = "";

		ArrayList<RestrictionCustom> restricciones = new ArrayList<RestrictionCustom>();

		Node attributes = basicDomainNode.getFirstChild();

		while (attributes != null) {
			if (attributes.getNodeName().compareTo("BasicType") == 0) {
				basicType = attributes.getFirstChild().getNodeValue();
			}
			else if (attributes.getNodeName().compareTo("Restrictions") == 0) {
				restricciones = processRestrictions(attributes);
			}

			attributes = attributes.getNextSibling();
		}

		BasicDomain domain = new BasicDomain(name, basicType);

		for (int i = 0; i < restricciones.size(); ++i) {
			domain.addRestriction((RestrictionCustom)restricciones.get(i));
		}

		return domain;
	}

	/**
	 * Domain Restrictions Node Processor.
	 * 
	 * Method for parsing an domain restrictions xml definition (as a Node
	 * object) and obtaining an ArrayList of Restriction objects.
	 * 
	 * @param restrictionsNode node of the domain restrictions xml definition we want to process.
	 * 
	 * @returns An ArrayList with all the Restrictions.
	 */
	private static ArrayList<RestrictionCustom> processRestrictions(Node restrictionsNode)
	{
		String name = ""; String subclass = "";
		Integer value = new Integer(0);

		ArrayList<RestrictionCustom> restricciones = new ArrayList<RestrictionCustom>();

		Node attributes = restrictionsNode.getFirstChild();
		while (attributes != null) {
			if (attributes.getNodeName().compareTo("Restriction") == 0)
			{
				Node restriccion = attributes.getFirstChild();
				while (restriccion != null) {
					if (restriccion.getNodeName().compareTo("Name") == 0) {
						name = restriccion.getFirstChild().getNodeValue();
					}
					else if (restriccion.getNodeName().equalsIgnoreCase("RestrictionSubClass")) {
						subclass = restriccion.getFirstChild().getNodeValue();
					}
					else if (restriccion.getNodeName().compareTo("Value") == 0) {
						value = new Integer(restriccion.getFirstChild().getNodeValue());
					}

					restriccion = restriccion.getNextSibling();
				}

				if (subclass.compareTo("LongitudCampoIgual") == 0) {
					restricciones.add(new FieldSizeEqualRestrictionCustom(name, value));
				}
				else if (subclass.compareTo("LongitudCampoMayor") == 0) {
					restricciones.add(new FieldSizeGreaterRestrictionCustom(name, value));
				}
				else if (subclass.compareTo("LongitudCampoMenor") == 0) {
					restricciones.add(new FieldSizeLessRestrictionCustom(name, value));
				}
				else if (subclass.compareTo("Numerico") == 0) {
					restricciones.add(new NumericFieldRestrictionCustom(name));
				}
				else if (subclass.compareTo("LongitudDecimales") == 0) {
					restricciones.add(new DecimalSizeRestrictionCustom(name, value));
				}
				else if (subclass.compareTo("MayorQue") == 0) {
					restricciones.add(new GreaterThanRestrictionCustom(name, new Float(value.toString())));
				}
				else if (subclass.compareTo("MenorQue") == 0) {
					restricciones.add(new LessThanRestrictionCustom(name, new Float(value.toString())));
				}

			}

			attributes = attributes.getNextSibling();
		}

		return restricciones;
	}
}
