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

package es.udc.cartolab.gvsig.eielforms.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class UserDomain extends Domain
{
  private HashMap values;
  private HashMap keys;
  private Set keySet;
  private String description;

  public UserDomain(String name, HashMap values)
  {
    super(name, "usuario");

    this.values = values;
    this.keySet = values.keySet();

    Iterator clavesIterator = this.keySet.iterator();
    this.description = new String(name + " (");

    this.keys = new HashMap();
    String oneValue = new String();
    while (clavesIterator.hasNext())
    {
      oneValue = (String)clavesIterator.next();

      this.keys.put(values.get(oneValue), oneValue);
      description = description + oneValue + ", ";
    }

    int pos = description.lastIndexOf(',');
    String finalChar = ")";
    if (pos<0) {
    	pos = description.length()-2;
    	finalChar = "";
    }
    description = description.substring(0, pos) + finalChar;
  }

  public boolean validate(String value)
  {
    boolean encontrado = false;

    Iterator clavesIterator = this.keySet.iterator();
    while ((clavesIterator.hasNext()) && (!(encontrado))) {
      if (value.compareTo((String)clavesIterator.next()) != 0) continue; encontrado = true;
    }
    return encontrado;
  }

  public ArrayList getValues() {
    ArrayList valoresArrayList = new ArrayList();
    Iterator clavesIterator = this.keySet.iterator();
    while (clavesIterator.hasNext()) {
      valoresArrayList.add(this.values.get(clavesIterator.next()));
    }
    return valoresArrayList;
  }

  public ArrayList getKeys() {
    ArrayList valoresArrayList = new ArrayList();
    Iterator clavesIterator = this.keySet.iterator();
    while (clavesIterator.hasNext()) {
      valoresArrayList.add(clavesIterator.next());
    }
    return valoresArrayList;
  }

  public String toString() {
    return new String(this.description);
  }

  public String resolve(String key) {
    return ((String)this.values.get(key));
  }

  public String unResolve(String value) {
    return ((String)this.keys.get(value));
  }
}
