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

package es.udc.cartolab.gvsig.eielforms.groups;

import java.util.ArrayList;

import org.w3c.dom.Node;

import es.udc.cartolab.gvsig.eielforms.field.FieldGenerator;
import es.udc.cartolab.gvsig.eielforms.field.FieldInterface;

public class GroupGenerator
{
	private ArrayList fields;
	private FieldGenerator fieldGenerator;

	public GroupGenerator()
	{
		this.fieldGenerator = new FieldGenerator();
	}

	public FieldGroup processGroup(Node groupNode)
	{
		ArrayList gruposContenidos = new ArrayList();
		this.fields = new ArrayList();
		FieldGroup grupo = new SingleFieldGroup("vacio", "gridbaglayout");
		String layout = "gridbaglayout";

		String name = groupNode.getAttributes().getNamedItem("GroupName").getNodeValue();
		String groupSubClass = groupNode.getAttributes().getNamedItem("GroupSubClass").getNodeValue();

		Node contenidoGrupo = groupNode.getFirstChild();
		while (contenidoGrupo != null) {
			if (contenidoGrupo.getNodeName().compareTo("Layout") == 0) {
				layout = contenidoGrupo.getFirstChild().getNodeValue();
			}
			else if (contenidoGrupo.getNodeName().compareTo("ContainedSimpleGroup") == 0) {
				gruposContenidos.add(processSingleFieldGroup(contenidoGrupo));
			}
			else if (contenidoGrupo.getNodeName().compareTo("ContainedComplexGroup") == 0) {
				gruposContenidos.add(processComplexFieldGroup(contenidoGrupo));
			}

			contenidoGrupo = contenidoGrupo.getNextSibling();
		}

		if (groupSubClass.compareTo("ThumbIndex") == 0) {
			grupo = new TabbedPaneGroupInterface(name, layout);
		}
		else if (groupSubClass.compareTo("Panel") == 0) {
			grupo = new BorderPaneGroupInterface(name, layout);
		} else {
			grupo = new BorderPaneGroupInterface(name, layout);
		}

		for (int i = 0; i < gruposContenidos.size(); ++i) {
			((ComplexFieldGroup)grupo).addGroup((FieldGroup)gruposContenidos.get(i));
		}

		return grupo;
	}

	private FieldGroup processSingleFieldGroup(Node singleFieldGroupNode)
	{
		this.fields = new ArrayList();
		String subGrupoLayout = new String("gridbaglayout");

		String subGrupoName = singleFieldGroupNode.getAttributes().getNamedItem("GroupName").getNodeValue();
		Node atributos = singleFieldGroupNode.getFirstChild();

		while (atributos != null) {
			if (atributos.getNodeName().compareTo("Layout") == 0) {
				subGrupoLayout = atributos.getFirstChild().getNodeValue();
			}
			else if (atributos.getNodeName().compareTo("Fields") == 0) {
				processFields(atributos);
			}

			atributos = atributos.getNextSibling();
		}

		FieldGroup subgrupo = new SingleFieldGroup(subGrupoName, subGrupoLayout);

		for (int i = 0; i < this.fields.size(); ++i) {
			FieldInterface fieldInterface = (FieldInterface)this.fields.get(i);
			((SingleFieldGroup)subgrupo).addField(fieldInterface);
		}

		return subgrupo;
	}

	private FieldGroup processComplexFieldGroup(Node complexFieldGroupNode)
	{
		ArrayList subgrupos = new ArrayList();
		String subGrupoLayout = new String("gridbaglayout");

		String subGrupoName = complexFieldGroupNode.getAttributes().getNamedItem("GroupName").getNodeValue();
		String subGroupSubClass = complexFieldGroupNode.getAttributes().getNamedItem("GroupSubClass").getNodeValue();

		Node atributos = complexFieldGroupNode.getFirstChild();

		while (atributos != null) {
			if (atributos.getNodeName().compareTo("Layout") == 0) {
				subGrupoLayout = atributos.getFirstChild().getNodeValue();
			}
			else if (atributos.getNodeName().compareTo("ContainedSimpleGroup") == 0) {
				subgrupos.add(processSingleFieldGroup(atributos));
			}
			else if (atributos.getNodeName().compareTo("ContainedComplexGroup") == 0) {
				subgrupos.add(processComplexFieldGroup(atributos));
			}

			atributos = atributos.getNextSibling();
		}
		FieldGroup subgrupo;
		if (subGroupSubClass.compareTo("ThumbIndex") == 0) {
			subgrupo = new TabbedPaneGroupInterface(subGrupoName, subGrupoLayout);
		}
		else
		{
			if (subGroupSubClass.compareTo("Block") == 0) {
				subgrupo = new BorderPaneGroupInterface(subGrupoName, subGrupoLayout);
			} else {
				subgrupo = new SinglePaneGroupInterface(subGrupoName, subGrupoLayout);
			}
		}

		for (int i = 0; i < subgrupos.size(); ++i) {
			((ComplexFieldGroup)subgrupo).addGroup((FieldGroup)subgrupos.get(i));
		}

		return subgrupo;
	}

	private void processFields(Node fieldsNode)
	{
		Node atributos = fieldsNode.getFirstChild();
		while (atributos != null) {
			if (atributos.getNodeName().compareTo("Field") == 0) {
				this.fields.add(this.fieldGenerator.processField(atributos));
			}
			atributos = atributos.getNextSibling();
		}
	}
}
