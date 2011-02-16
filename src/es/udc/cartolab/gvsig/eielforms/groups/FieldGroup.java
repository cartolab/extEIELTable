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

import javax.swing.JComponent;

import es.udc.cartolab.gvsig.eielforms.field.FieldInterface;

public abstract class FieldGroup
{
  protected String name;
  protected String layout;

  public FieldGroup(String groupName, String layout)
  {
    this.name = groupName;
    this.layout = layout.toUpperCase();
  }

  public String getName() {
    return this.name;
  }

  public String getLayout() {
    return this.layout;
  }

  public abstract ArrayList getFields();

  public abstract ArrayList getFieldsInterface();

  public abstract ArrayList getKey();

  public abstract JComponent getInterface();

  public abstract void refresh();

  public abstract void enableFields(boolean paramBoolean);

  public abstract boolean validate();

  public abstract void initFields();

  public abstract void loadData();

  public abstract void saveInMemory();

  public abstract void loadMemory();

  public FieldInterface getFieldInterface(String fieldName) {

	  ArrayList fields = getFieldsInterface();
	  for (int i=0; i<fields.size(); i++) {
		  FieldInterface fi = (FieldInterface) fields.get(i);
		  if (fi.getField().getName().equals(fieldName)) {
			  return fi;
		  }
	  }

	  return null;

  }
}
