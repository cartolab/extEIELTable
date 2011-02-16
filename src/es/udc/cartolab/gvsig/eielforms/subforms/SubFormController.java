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

import es.udc.cartolab.gvsig.eielforms.forms.FormInterface;

public class SubFormController
{
  private boolean knowKey;
  private String dataBase;
  private String table;
  private String layer;
  private ArrayList key;
  private SubFormInterface subformInterface;
  private String name;

  public SubFormController(FormInterface formInterface, SubForm subform, String layer, String dataBase, String table, String layout, String name, String title)
  {
    this.layer = layer;
    this.dataBase = dataBase;
    this.table = table;
    this.name = name;

    this.knowKey = false;
    this.key = new ArrayList();
    this.subformInterface = new SubFormInterface(formInterface, subform, this, layout, title);
  }

  public String getDataBase() {
    return this.dataBase;
  }

  public String getTable() {
    return this.table;
  }

  public SubFormInterface getInterface() {
    return this.subformInterface;
  }

  public String getLayer() {
    return this.layer;
  }

  public String getName() {
    return this.name;
  }
}
