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

package es.udc.cartolab.gvsig.eieltable.util;

import java.util.Vector;

public class OtrosDatosVO
{
  private String codigo;
  private Vector datos;

  public OtrosDatosVO()
  {
    this.codigo = "";
    this.datos = new Vector();
  }

  public String getCodigo()
  {
    return this.codigo;
  }

  public void setCodigo(String codigo)
  {
    this.codigo = codigo;
  }

  public Vector getDatos()
  {
    return this.datos;
  }

  public void setDatos(Vector datos)
  {
    this.datos = datos;
  }
}
