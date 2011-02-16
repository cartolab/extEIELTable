/*
 * Copyright (c) 2010. Cartolab (Universidade da Coru�a)
 *
 * This file is part of extEIELForms
 *
 * extEIELForms is based on the forms application of GisEIEL <http://giseiel.forge.osor.eu/>
 * devoloped by Laboratorio de Bases de Datos (Universidade da Coru�a)
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

package es.udc.cartolab.gvsig.eieltable.forms.listener;

import es.udc.cartolab.gvsig.eieltable.field.FieldInterface;
import es.udc.cartolab.gvsig.eieltable.field.listener.FieldChangeEvent;
import es.udc.cartolab.gvsig.eieltable.forms.FormController;

public class FormChangeEvent {

	private FormController form;
	private FieldChangeEvent fieldEvent;

	public FormChangeEvent(FormController form, FieldChangeEvent e) {

		this.form = form;
		this.fieldEvent = e;

	}

	public FormController getForm() {
		return form;
	}

	public FieldInterface getField() {
		return fieldEvent.getField();
	}

	public String getOldValue() {
		return fieldEvent.getOldValue();
	}

	public String getNewValue() {
		return fieldEvent.getNewValue();
	}
}
