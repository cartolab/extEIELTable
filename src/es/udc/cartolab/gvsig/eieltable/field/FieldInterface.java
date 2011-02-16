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

package es.udc.cartolab.gvsig.eieltable.field;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;

import es.udc.cartolab.gvsig.eieltable.field.listener.FieldChangeEvent;
import es.udc.cartolab.gvsig.eieltable.field.listener.FieldChangeListener;

public abstract class FieldInterface
{
	protected FieldController fieldController;
	private JLabel fieldLabel;
	protected ArrayList<FieldChangeListener> listeners = new ArrayList<FieldChangeListener>();
	private String oldValue = null;

	public FieldInterface(FieldController fieldController)
	{
		this.fieldController = fieldController;
		this.fieldLabel = new JLabel(fieldController.getLabel());
		this.fieldLabel.setPreferredSize(new Dimension(150, 20));
		String toolTip = new String(fieldController.getLabel() + " ["+ fieldController.getDomain().toString());
		if (fieldController.getRequired() == true) {
			toolTip = toolTip + ", obligatorio";
		}
		toolTip = toolTip + "]";
		this.fieldLabel.setToolTipText(toolTip);
	}

	public JLabel getLabel() {
		return this.fieldLabel;
	}

	public FieldController getField() {
		return this.fieldController;
	}

	public boolean validate() {
		boolean esValido = this.fieldController.validate();

		if (!esValido) {
			highlight(true);
		} else {
			highlight(false);
		}

		return esValido;
	}

	private void highlight(boolean highlighted) {
		if (highlighted == true) {
			this.fieldLabel.setForeground(Color.red);
		} else {
			this.fieldLabel.setForeground(Color.black);
		}
		this.fieldLabel.repaint();
	}

	public abstract JComponent getComponent();

	public abstract boolean fillField();

	public abstract void enableField(boolean paramBoolean);

	public abstract void loadValue();

	public abstract FieldInterface clonar();

	public void loadDefaultValue() {
		this.fieldController.setValue(this.fieldController.getDefaultValue());

		System.out.println("loadDefaultValue: " + this.fieldController.getName() + " <-> " + this.fieldController.getDefaultValue());

		loadValue();
		highlight(false);
	}

	public abstract void saveInMemory();

	public void loadMemory() {
		this.fieldController.setValue(this.fieldController.getMemoryValue());
		loadValue();
		highlight(false);
	}

	public void setValue(String value) {
		this.fieldController.setValue(value);

		loadValue();
	}

	/**
	 * Calls all objects which are listening to changes on the fields.
	 *
	 * This method should be called <b>after</b> setting the new value on the field interface. Beware of fillField method.
	 * @param newValue
	 */
	protected void fireFieldChanged() {
		String oldValue = fieldController.getOldValue();
		String newValue = fieldController.getValue();
		if (newValue == null) {
			newValue = "";
		}
		if (oldValue == null) {
			oldValue = "";
		}
		if (!oldValue.equals(newValue)) {
			FieldChangeEvent e = new FieldChangeEvent(this, oldValue, newValue);
			fieldController.setOldValue(newValue);
			for (FieldChangeListener list : listeners) {
				list.fieldChanged(e);
			}
		}
	}

	public void addFieldChangeListener(FieldChangeListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removeFieldChangeListener(FieldChangeListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}
}
