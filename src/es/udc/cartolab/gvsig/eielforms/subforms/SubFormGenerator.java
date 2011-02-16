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

package es.udc.cartolab.gvsig.eielforms.subforms;


import java.util.ArrayList;
import org.w3c.dom.Node;

import es.udc.cartolab.gvsig.eielforms.dependency.DependencyMasterFieldGenerator;
import es.udc.cartolab.gvsig.eielforms.field.FieldGenerator;
import es.udc.cartolab.gvsig.eielforms.field.FieldInterface;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class SubFormGenerator
{
  private DependencyMasterFieldGenerator dependencyMasterFieldGenerator;
  private ArrayList foreignKey;
  private ArrayList fields;
  private ArrayList primaryKey;
  private FieldGenerator fieldGenerator;
  private Node masterFieldNode;

  public SubFormGenerator()
  {
    this.fieldGenerator = new FieldGenerator();
  }

  public SubForm processSubForm(Node subformNode)
  {
    FieldInterface primaryField = null;

    this.masterFieldNode = null;
    this.foreignKey = new ArrayList();
    this.fields = new ArrayList();
    String layout = "flowlayout";
    String tabla = "";
    String database = "";
    String name = "";

    Node contenidoSubform = subformNode.getFirstChild();
    
    DBSession dbs  = DBSession.getCurrentSession();
	if (dbs != null) {
		database = dbs.getSchema();
	} else {
		database = contenidoSubform.getAttributes().getNamedItem("DataBase").getNodeValue();
	}
    while (contenidoSubform != null) {
     
      if (contenidoSubform.getNodeName().compareTo("Name") == 0) {
        name = contenidoSubform.getFirstChild().getNodeValue();
      }
      else if (contenidoSubform.getNodeName().compareTo("Table") == 0) {
        tabla = contenidoSubform.getFirstChild().getNodeValue();
      }
      else if (contenidoSubform.getNodeName().compareTo("Layout") == 0) {
        layout = contenidoSubform.getFirstChild().getNodeValue();
      }
      else if (contenidoSubform.getNodeName().compareTo("ForeignKey") == 0) {
        processForeignKey(contenidoSubform);
      }
      else if (contenidoSubform.getNodeName().compareTo("PrimaryField") == 0) {
        Node foreignKeyAttributes = contenidoSubform.getFirstChild();
        while (foreignKeyAttributes.getNodeName().compareTo("Field") != 0) {
          foreignKeyAttributes = foreignKeyAttributes.getNextSibling();
        }
        if (foreignKeyAttributes.getNodeName().compareTo("Field") == 0) {
          primaryField = this.fieldGenerator.processField(foreignKeyAttributes);
        }

      }
      else if (contenidoSubform.getNodeName().compareTo("Fields") == 0) {
        processFields(contenidoSubform);
      }

      contenidoSubform = contenidoSubform.getNextSibling();
    }

    SubForm subForm = new SubForm(name, tabla, database, this.foreignKey, primaryField, this.fields);
    for (int i = 0; i < this.fields.size(); ++i) {
      FieldInterface fieldInterface = (FieldInterface)this.fields.get(i);
      subForm.addField(fieldInterface);
    }

    return subForm;
  }

  private void processForeignKey(Node foreignKeyNode)
  {
    Node foreignKeyAttributes = foreignKeyNode.getFirstChild();
    while (foreignKeyAttributes != null) {
      if (foreignKeyAttributes.getNodeName().compareTo("ForeignKeyField") == 0) {
        this.foreignKey.add(foreignKeyAttributes.getFirstChild().getNodeValue());
      }
      foreignKeyAttributes = foreignKeyAttributes.getNextSibling();
    }
  }

  private void processFields(Node fieldsNode)
  {
    Node foreignKeyAttributes = fieldsNode.getFirstChild();
    while (foreignKeyAttributes != null) {
      if (foreignKeyAttributes.getNodeName().compareTo("Field") == 0) {
        this.fields.add(this.fieldGenerator.processField(foreignKeyAttributes));
      }
      foreignKeyAttributes = foreignKeyAttributes.getNextSibling();
    }
  }
}
