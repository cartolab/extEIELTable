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

import es.udc.cartolab.gvsig.eielforms.domain.restriction.DecimalSizeRestriction;
import es.udc.cartolab.gvsig.eielforms.domain.restriction.NumericFieldRestriction;
import es.udc.cartolab.gvsig.eielforms.domain.restriction.Restriction;

public class BasicDomain extends Domain
{
  private String tipoBase;
  private ArrayList<Restriction> restrictiones;
  private String descripcion;

  public BasicDomain(String name, String tipoBase)
  {
    super(name, "basico");
    this.tipoBase = tipoBase;
    this.restrictiones = new ArrayList();
    //three tipoBase kinds: string, int, numeric (float?)
    if (this.tipoBase.equalsIgnoreCase("int") || this.tipoBase.equalsIgnoreCase("numeric")) {
    	restrictiones.add(new NumericFieldRestriction("tipoBase"));
    	if (this.tipoBase.equalsIgnoreCase("int")) {
    		//no decimal count allowed
    		restrictiones.add(new DecimalSizeRestriction("tipoBase", 0));
    	}
    }
    this.descripcion = new String(tipoBase);
  }

  public boolean validate(String valor) {
    int i = 0;
    boolean valid = true;

    for (i = 0; i < this.restrictiones.size(); ++i) {
      valid = (valid) && ((this.restrictiones.get(i)).validate(valor));
    }
    return valid;
  }

  public void addRestriction(Restriction restriction)
  {
    this.restrictiones.add(restriction);

    descripcion = descripcion + ", " + restriction.toString();
  }

  public ArrayList<Restriction> getRestrictions() {
    return this.restrictiones;
  }

  public String toString() {
    return new String(this.descripcion);
  }


}
