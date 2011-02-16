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

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class TextFieldInterface extends FieldInterface
{
	private int WIDTH_DEFAULT = 200;
	private JTextField textField;

	public TextFieldInterface(FieldController fieldController)
	{
		super(fieldController);
		this.textField = new JTextField(fieldController.getDefaultValue());
		this.textField.setPreferredSize(new Dimension(this.WIDTH_DEFAULT, 20));

		this.textField.setEnabled(fieldController.getEditable());

		this.textField.addFocusListener(new FocusLostAction(this));
	}

	@Override
	public JComponent getComponent()
	{
		return this.textField;
	}

	@Override
	public boolean fillField() {
		this.fieldController.setValue(this.textField.getText());
		return true;
	}

	@Override
	public void enableField(boolean enabled) {
		if (enabled == true) {
			if (this.fieldController.getEditable() == true) {
				this.textField.setEnabled(enabled);
			}
		} else {
			this.textField.setEnabled(enabled);
		}
	}

	@Override
	public void loadValue()
	{
		String value = this.fieldController.getValue();
		this.textField.setText(value);
		validate();
	}

	@Override
	public void saveInMemory() {
		this.fieldController.setMemoryValue(this.textField.getText());
	}

	@Override
	public FieldInterface clonar()
	{
		FieldController field = this.fieldController.clonar();
		field.setEditable(false);
		TextFieldInterface textField = new TextFieldInterface(field);
		return textField;
	}

	private class FocusLostAction
	implements FocusListener
	{
		private TextFieldInterface textFieldInterface;

		public FocusLostAction(TextFieldInterface paramTextFieldInterface)
		{
			this.textFieldInterface = paramTextFieldInterface;
		}

		public void focusLost(FocusEvent evt) {
			this.textFieldInterface.fillField();
			fireFieldChanged();
			this.textFieldInterface.validate();
		}

		public void focusGained(FocusEvent evt)
		{
		}
	}
}
