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

import java.util.ArrayList;
import java.util.HashMap;

import es.udc.cartolab.gvsig.eieltable.forms.FormController;

public class FormCache
{
	private HashMap cache;
	private ArrayList keys;
	private int size;

	public FormCache(Integer size)
	{
		this.cache = new HashMap();
		this.keys = new ArrayList();
		this.size = size.intValue();
	}

	public void addFormController(String layer, FormController formController) {
		this.cache.put(layer, formController);
		this.keys.add(0, layer);

		if (this.keys.size() > this.size) {
			this.cache.remove(this.keys.get(this.size));
			this.keys.remove(this.size);
		}
	}

	public FormController getFormController(String layer) {
		return (FormController)this.cache.get(layer);
	}

	public void resetCache() {
		this.cache.clear();
	}
}
