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

import es.udc.cartolab.gvsig.eieltable.domain.BasicDomain;
import es.udc.cartolab.gvsig.eieltable.domain.Domain;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.DecimalSizeRestriction;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.FieldSizeEqualRestriction;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.FieldSizeGreaterRestriction;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.FieldSizeLessRestriction;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.GreaterThanRestriction;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.LessThanRestriction;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.NumericFieldRestriction;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.Restriction;
import es.udc.cartolab.gvsig.eieltable.formgenerator.FormException;

public class DomainGenerator
{
	private DomainReader domainDAO;
	private DOMParser domParser;
	private Domain domain;
	private DomainCache domainCache;

	public DomainGenerator()
	{
		this.domainDAO = new DomainDBReader();
		this.domParser = new DOMParser();
		this.domainCache = new DomainCache();
	}

	public Domain getDomain(String domainName)
	{
		String XMLDefinition = "";

		this.domain = this.domainCache.getDomain(domainName);
		if (this.domain == null) {
			try {
				this.domain = new BasicDomain(domainName, "string");

				XMLDefinition = this.domainDAO.getDomainDefinition(domainName);

				if (XMLDefinition != null) {
					StringReader sr = new StringReader(XMLDefinition);
					InputSource is = new InputSource(sr);

					this.domParser.parse(is);
					Node nodoRaiz = this.domParser.getDocument();

					processDomain(nodoRaiz);
					this.domainCache.addDomain(domainName, this.domain);
				} else {
					System.out.println("El dominio " + domainName + " no ha sido encontrado !!!");
					System.out.println("Crearemos un domain basico tipo String por defecto");
				}
			}
			catch (Exception e) {
				System.out.println("Algun problema procesando el dominio " + domainName + "!!!");
				e.printStackTrace();
			}
		} else {
			System.out.println("Recuperado el dominio " + this.domain.getName() + " de la cache");
		}

		return this.domain;
	}

	private void processDomain(Node rootNode)
	{
		Node domainNode = rootNode.getFirstChild();

		if (domainNode.getNodeName().compareTo("domain") == 0) {
			String name = domainNode.getAttributes().getNamedItem("name").getNodeValue();
			Node descriptionNode = domainNode.getFirstChild();
			while (descriptionNode != null) {
				if (descriptionNode.getNodeName().compareTo("UserDomain") == 0) {
					processUserDomain(name, descriptionNode);
				}
				else if (descriptionNode.getNodeName().compareTo("BasicDomain") == 0) {
					processBasicDomain(name, descriptionNode);
				}

				descriptionNode = descriptionNode.getNextSibling();
			}
		}
	}

	private void processUserDomain(String name, Node userDomainNode)
	{
		String esquema = "";
		String table = "";

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
			this.domain = this.domainDAO.getDomain(name, esquema, table);
		}
		catch (FormException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ha ocurrido un error al generar el dominio " + name);
		}
	}

	private void processBasicDomain(String name, Node basicDomainNode)
	{
		String basicType = "";

		ArrayList restricciones = new ArrayList();

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

		this.domain = new BasicDomain(name, basicType);

		for (int i = 0; i < restricciones.size(); ++i) {
			((BasicDomain)this.domain).addRestriction((Restriction)restricciones.get(i));
		}
	}

	private ArrayList processRestrictions(Node restrictionsNode)
	{
		String name = ""; String subclass = "";
		Integer value = new Integer(0);

		ArrayList restricciones = new ArrayList();

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
					restricciones.add(new FieldSizeEqualRestriction(name, value));
				}
				else if (subclass.compareTo("LongitudCampoMayor") == 0) {
					restricciones.add(new FieldSizeGreaterRestriction(name, value));
				}
				else if (subclass.compareTo("LongitudCampoMenor") == 0) {
					restricciones.add(new FieldSizeLessRestriction(name, value));
				}
				else if (subclass.compareTo("Numerico") == 0) {
					restricciones.add(new NumericFieldRestriction(name));
				}
				else if (subclass.compareTo("LongitudDecimales") == 0) {
					restricciones.add(new DecimalSizeRestriction(name, value));
				}
				else if (subclass.compareTo("MayorQue") == 0) {
					restricciones.add(new GreaterThanRestriction(name, new Float(value.toString())));
				}
				else if (subclass.compareTo("MenorQue") == 0) {
					restricciones.add(new LessThanRestriction(name, new Float(value.toString())));
				}

			}

			attributes = attributes.getNextSibling();
		}

		return restricciones;
	}
}
