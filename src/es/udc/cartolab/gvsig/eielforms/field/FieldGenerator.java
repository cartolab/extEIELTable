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

package es.udc.cartolab.gvsig.eielforms.field;

import org.w3c.dom.Node;

import es.udc.cartolab.gvsig.eielforms.domain.Domain;
import es.udc.cartolab.gvsig.eielforms.domain.generator.DomainGenerator;
import es.udc.cartolab.gvsig.eielutils.constants.Constants;

public class FieldGenerator
{
	private DomainGenerator domainGenerator;

	public FieldGenerator()
	{
		this.domainGenerator = new DomainGenerator();
	}

	public FieldInterface processField(Node fieldNode)
	{
		String name = new String();
		String label = new String();
		String string_dominio = new String("");
		String isKey = new String();
		String editable = new String();
		String required = new String();
		String save = new String();
		String defaultValue = null;
		boolean bool_save = false;
		boolean bool_isKey = false;
		boolean bool_editable = false;
		boolean bool_required = false;
		boolean bool_constant_value = false;
		String isOrden = "";
		boolean bool_isOrden = false;
		boolean bool_isLength = false;
		boolean bool_isArea = false;
		String unitLength = "m";
		String unitArea = "m2";

		Node atributos = fieldNode.getFirstChild();

		while (atributos != null)
		{

			if (atributos.getNodeName().compareTo("Name") == 0) {
				name = atributos.getFirstChild().getNodeValue();
			}
			else if (atributos.getNodeName().compareTo("Label") == 0) {
				label = atributos.getFirstChild().getNodeValue();
			}
			else if (atributos.getNodeName().compareTo("Domain") == 0)
			{
				if (atributos.getFirstChild() != null) {
					string_dominio = atributos.getFirstChild().getNodeValue();
				} else {
					string_dominio = "";
				}

			}
			else if (atributos.getNodeName().compareTo("IsKey") == 0) {
				isKey = atributos.getFirstChild().getNodeValue();
				bool_isKey = getBoolean(isKey);
			}
			else if (atributos.getNodeName().compareTo("Editable") == 0) {
				editable = atributos.getFirstChild().getNodeValue();
				bool_editable = getBoolean(editable);
			}
			else if (atributos.getNodeName().compareTo("Required") == 0) {
				required = atributos.getFirstChild().getNodeValue();
				bool_required = getBoolean(required);
			}
			else if (atributos.getNodeName().compareTo("Save") == 0) {
				save = atributos.getFirstChild().getNodeValue();
				bool_save = getBoolean(save);
			}
			else if (atributos.getNodeName().compareTo("DefaultValue") == 0 &&
					atributos.getFirstChild() != null) {
				Node defaultValueTypeNode = atributos.getFirstChild();

				while (defaultValueTypeNode != null) {
					if (defaultValueTypeNode.getNodeName().compareTo("SingleValue") == 0) {
						bool_constant_value = false;
						Constants cts = Constants.getCurrentConstants();
						boolean constant = false;
						if (cts!=null) {
							String value = cts.getValue(name);
							if (value!=null) {
								defaultValue = value;
								constant = true;
							}
						}
						if (!constant) {
							defaultValue = defaultValueTypeNode.getFirstChild().getNodeValue();
						}
					}
					else if (defaultValueTypeNode.getNodeName().compareTo("ConstantValue") == 0) {
						bool_constant_value = true;
						Constants cts = Constants.getCurrentConstants();
						boolean constant = false;
						if (cts!=null) {
							String value = cts.getValue(name);
							if (value!=null) {
								defaultValue = value;
								constant = true;
							}
						}
						if (!constant) {
							defaultValue = defaultValueTypeNode.getFirstChild().getNodeValue();
						}
					}

					defaultValueTypeNode = defaultValueTypeNode.getNextSibling();
				}

			}
			else if (atributos.getNodeName().compareTo("IsOrden") == 0) {
				isOrden = atributos.getFirstChild().getNodeValue();
				bool_isOrden = getBoolean(isOrden);
			}
			else if (atributos.getNodeName().compareTo("IsLength") == 0) {
				unitLength = atributos.getAttributes().getNamedItem("unit").getNodeValue();
				String isLength = atributos.getFirstChild().getNodeValue();
				bool_isLength = getBoolean(isLength);
			}
			else if (atributos.getNodeName().compareTo("IsArea") == 0) {
				unitArea = atributos.getAttributes().getNamedItem("unit").getNodeValue();
				String isArea = atributos.getFirstChild().getNodeValue();
				bool_isArea = getBoolean(isArea);
			}

			atributos = atributos.getNextSibling();
		}

		if (defaultValue == null) {
			Constants cts = Constants.getCurrentConstants();
			boolean constant = false;
			if (cts!=null) {
				String value = cts.getValue(name);
				if (value!=null) {
					defaultValue = value;
					constant = true;
				}
			}
			if (!constant) {
				defaultValue = new String();
			}
		}

		Domain domain = this.domainGenerator.getDomain(string_dominio);

		FieldController fieldController = new FieldController(label, name, domain, defaultValue, bool_editable, bool_required, bool_isKey, bool_constant_value, bool_isOrden);
		fieldController.setMustSave(bool_save);
		fieldController.setIsLength(bool_isLength, unitLength);
		fieldController.setIsArea(bool_isArea, unitArea);
		FieldInterface fieldInterface;
		if (domain.getType().compareTo("usuario") == 0)
		{
			fieldInterface = new ComboFieldInterface(fieldController);
		}
		else {
			fieldInterface = new TextFieldInterface(fieldController);
		}
		return fieldInterface;
	}

	private boolean getBoolean(String value)
	{
		boolean valorBooleano;
		if (value.toUpperCase().compareTo("TRUE") == 0) {
			valorBooleano = true;
		} else {
			valorBooleano = false;
		}

		return valorBooleano;
	}
}
