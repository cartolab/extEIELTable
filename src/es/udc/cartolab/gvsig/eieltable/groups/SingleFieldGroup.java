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

package es.udc.cartolab.gvsig.eieltable.groups;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import es.udc.cartolab.gvsig.eieltable.field.FieldController;
import es.udc.cartolab.gvsig.eieltable.field.FieldInterface;

public class SingleFieldGroup extends FieldGroup
{
  protected ArrayList fields;
  private JPanel panel;
  private GridBagConstraints gridbagconst;

  public SingleFieldGroup(String groupName, String layout)
  {
    super(groupName, layout.toUpperCase());
    this.fields = new ArrayList();
    this.panel = new JPanel();

    configureLayout();
    updateLayout();
  }

  public JComponent getInterface() {
    return this.panel;
  }

  protected void fillFields()
  {
    for (int i = 0; i < this.fields.size(); ++i)
      ((FieldInterface)this.fields.get(i)).fillField();
  }

  public ArrayList getFields()
  {
    ArrayList temporalCampos = new ArrayList();

//    fillFields();
    for (int i = 0; i < this.fields.size(); ++i) {
      temporalCampos.add(((FieldInterface)this.fields.get(i)).getField());
    }

    return temporalCampos;
  }

  public ArrayList getFieldsInterface()
  {
    ArrayList temporalCampos = new ArrayList();

//    fillFields();
    for (int i = 0; i < this.fields.size(); ++i) {
      temporalCampos.add(this.fields.get(i));
    }

    return temporalCampos;
  }

  public ArrayList getKey()
  {
    ArrayList temporalCampos = new ArrayList();

//    fillFields();

    for (int i = 0; i < this.fields.size(); ++i)
    {
      FieldController unCampo = ((FieldInterface)this.fields.get(i)).getField();
      if (unCampo.getIsKey() == true) {
        temporalCampos.add(unCampo);
      }
    }

    return temporalCampos;
  }

  public void addField(FieldInterface fieldInterface) {
    this.fields.add(fieldInterface);
    if (this.layout.compareTo("FLOWLAYOUT") == 0) {
      this.panel.add(fieldInterface.getLabel());
      this.panel.add(fieldInterface.getComponent());
    } else {
      this.gridbagconst.gridx = 0;
      this.panel.add(fieldInterface.getLabel(), this.gridbagconst);
      this.gridbagconst.gridx = 1;
      this.panel.add(fieldInterface.getComponent(), this.gridbagconst);
      updateLayout();
    }
  }

  protected void configureLayout()
  {
    if (this.layout.compareTo("FLOWLAYOUT") == 0) {
      this.panel.setLayout(new FlowLayout());
    }
    else if (this.layout.compareTo("GRIDBAGLAYOUT") == 0) {
      this.panel.setLayout(new GridBagLayout());
      this.gridbagconst = new GridBagConstraints();

      this.gridbagconst.gridy = 0;
      this.gridbagconst.gridx = 0;
    }
  }

  private void updateLayout()
  {
    if (this.layout.compareTo("FLOWLAYOUT") == 0) {
      return;
    }
    if (this.layout.compareTo("GRIDBAGLAYOUT") == 0)
      this.gridbagconst.gridy += 1;
  }

  public void refresh()
  {
    this.panel.setVisible(true);
  }

  public void enableFields(boolean enabled)
  {
    for (int i = 0; i < this.fields.size(); ++i)
      ((FieldInterface)this.fields.get(i)).enableField(enabled);
  }

  public boolean validate()
  {
    boolean esValido = true;

    for (int i = 0; i < this.fields.size(); ++i) {
      esValido &= ((FieldInterface)this.fields.get(i)).validate();
    }
    return esValido;
  }

  public void initFields()
  {
    for (int i = 0; i < this.fields.size(); ++i)
      ((FieldInterface)this.fields.get(i)).loadDefaultValue();
  }

  public void loadData()
  {
    for (int i = 0; i < this.fields.size(); ++i)
      ((FieldInterface)this.fields.get(i)).loadValue();
  }

  public void saveInMemory()
  {
    for (int i = 0; i < this.fields.size(); ++i)
      ((FieldInterface)this.fields.get(i)).saveInMemory();
  }

  public void loadMemory()
  {
    for (int i = 0; i < this.fields.size(); ++i)
      ((FieldInterface)this.fields.get(i)).loadMemory();
  }
}
