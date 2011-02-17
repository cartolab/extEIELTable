/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
 *
 * This file is part of extEIELTable
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import es.udc.cartolab.gvsig.eieltable.domain.UserDomain;

public class ComboFieldInterface extends FieldInterface
{
	protected JComboBox comboField;
	protected ArrayList domainKeys;
	private int WIDTH_DEFAULT = 200;
	private UserDomain userDomain;

	public ComboFieldInterface(FieldController fieldController)
	{
		super(fieldController);

		this.userDomain = (UserDomain)fieldController.getDomain();

		this.domainKeys = this.userDomain.getKeys();
		ArrayList domainValues = this.userDomain.getValues();
		this.comboField = new JComboBox(new Vector(domainValues));
		this.comboField.setPreferredSize(new Dimension(this.WIDTH_DEFAULT, 20));
		this.comboField.setEnabled(fieldController.getEditable());
		this.comboField.setLightWeightPopupEnabled(false);
		this.comboField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent paramActionEvent) {
				// TODO Auto-generated method stub
				fillField();
				fireFieldChanged();
				validate();

			}

		});
	}

	@Override
	public JComponent getComponent() {
		return this.comboField;
	}

	@Override
	public boolean fillField() {
		this.fieldController.setValue(getIndexKey(this.comboField.getSelectedIndex()));
		return true;
	}

	@Override
	public void enableField(boolean enabled) {
		if (enabled == true) {
			if (this.fieldController.getEditable() == true) {
				this.comboField.setEnabled(enabled);
			}
		} else {
			this.comboField.setEnabled(enabled);
		}
	}

	@Override
	public void loadValue()
	{
		UserDomain userDomain = (UserDomain)this.fieldController.getDomain();

		this.comboField.setSelectedItem(userDomain.resolve(this.fieldController.getValue()));
		validate();
	}

	@Override
	public void saveInMemory() {
		this.fieldController.setMemoryValue(getIndexKey(this.comboField.getSelectedIndex())); }

	public UserDomain getDomain() {
		return this.userDomain;
	}

	protected String getIndexKey(int index)
	{
		try
		{
			String returnedKey;
			if (index < 0) {
				returnedKey = null;
			} else {
				returnedKey = (String)this.domainKeys.get(index);
			}
			return returnedKey;
		} catch (Exception e) {
			e.printStackTrace(); }
		return null;
	}

	@Override
	public FieldInterface clonar() {
		FieldController field = this.fieldController.clonar();
		field.setEditable(false);
		ComboFieldInterface comboField = new ComboFieldInterface(field);
		return comboField;
	}

}
