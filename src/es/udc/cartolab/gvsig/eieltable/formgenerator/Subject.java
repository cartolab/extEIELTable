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

package es.udc.cartolab.gvsig.eieltable.formgenerator;

import java.util.Vector;

public abstract class Subject
{
  protected Vector<Observer> observeres;

  public Subject()
  {
    this.observeres = new Vector<Observer>();
  }

  public void addObserver(Observer o)
  {
    this.observeres.addElement(o);
  }

  public void deleteObserver(Observer o)
  {
    this.observeres.removeElement(o);
  }

  public void notifyObservers() {
    for (int i = 0; i < this.observeres.size(); ++i) {
      Observer o = this.observeres.elementAt(i);
      o.update(this);
    }
  }

  public void notifyDeletionToObservers() {
    for (int i = 0; i < this.observeres.size(); ++i) {
      Observer o = this.observeres.elementAt(i);
      o.updateDeletion(this);
    }
  }
}
