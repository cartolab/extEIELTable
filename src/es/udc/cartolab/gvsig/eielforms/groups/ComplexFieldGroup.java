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

public abstract class ComplexFieldGroup extends FieldGroup
{
  protected ArrayList groups;
  protected JComponent interfazGrupo;

  protected ComplexFieldGroup(String groupName, String layout, JComponent groupInterface)
  {
    super(groupName, layout);
    this.groups = new ArrayList();
    this.interfazGrupo = groupInterface;
  }

  public ArrayList getFields()
  {
    ArrayList temporal = new ArrayList();

    for (int i = 0; i < this.groups.size(); ++i) {
      temporal.addAll(((FieldGroup)this.groups.get(i)).getFields());
    }
    return temporal;
  }

  public ArrayList getFieldsInterface()
  {
    ArrayList temporal = new ArrayList();

    for (int i = 0; i < this.groups.size(); ++i) {
      temporal.addAll(((FieldGroup)this.groups.get(i)).getFieldsInterface());
    }
    return temporal;
  }

  public ArrayList getKey()
  {
    ArrayList temporal = new ArrayList();

    for (int i = 0; i < this.groups.size(); ++i) {
      temporal.addAll(((FieldGroup)this.groups.get(i)).getKey());
    }
    return temporal;
  }

  public JComponent getInterface() {
    return this.interfazGrupo;
  }

  public abstract void addGroup(FieldGroup paramFieldGroup);

  public void refresh() {
    this.interfazGrupo.setVisible(true);
  }

  public void enableFields(boolean enabled)
  {
    for (int i = 0; i < this.groups.size(); ++i)
      ((FieldGroup)this.groups.get(i)).enableFields(enabled);
  }

  public boolean validate()
  {
    boolean esValido = true;

    for (int i = 0; i < this.groups.size(); ++i) {
      esValido &= ((FieldGroup)this.groups.get(i)).validate();
    }
    return esValido;
  }

  public void initFields()
  {
    for (int i = 0; i < this.groups.size(); ++i)
      ((FieldGroup)this.groups.get(i)).initFields();
  }

  public void loadData()
  {
    for (int i = 0; i < this.groups.size(); ++i)
      ((FieldGroup)this.groups.get(i)).loadData();
  }

  public void saveInMemory()
  {
    for (int i = 0; i < this.groups.size(); ++i)
      ((FieldGroup)this.groups.get(i)).saveInMemory();
  }

  public void loadMemory()
  {
    for (int i = 0; i < this.groups.size(); ++i)
      ((FieldGroup)this.groups.get(i)).loadMemory();
  }
}
