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

package es.udc.cartolab.gvsig.eieltable.dependency;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import es.udc.cartolab.gvsig.eieltable.domain.UserDomain;
import es.udc.cartolab.gvsig.eieltable.field.ComboFieldInterface;
import es.udc.cartolab.gvsig.eieltable.field.FieldController;

public class DependencyMasterField extends ComboFieldInterface
{
	private LinkedHashMap dependencyValues;
	private Dependency dependency;
	private String visibleFieldName;
	private String foreignField;
	private Collection secondaryFields;

	public DependencyMasterField(FieldController fieldController, Dependency dependency, LinkedHashMap dependencyValues, String visibleFieldName, Collection secondaryFields, String foreignField)
	{
		super(fieldController);
		this.dependencyValues = dependencyValues;
		this.comboField.addActionListener(new ComboFieldChangeAction());
		this.dependency = dependency;
		this.visibleFieldName = visibleFieldName;

		if (foreignField.compareTo("") == 0) {
			this.foreignField = fieldController.getName();
		} else {
			this.foreignField = foreignField;
		}
		this.secondaryFields = secondaryFields;
	}

	@Override
	public void loadValue() {
		UserDomain userDomain = (UserDomain)this.fieldController.getDomain();
		this.comboField.setSelectedItem(userDomain.resolve(this.fieldController.getValue()));
		validate();
	}

	@Override
	public void loadDefaultValue()
	{
		String defaultValueToLoad = new String();

		//TODO constants...
		//    Iterator itemIterator = this.secondaryFields.iterator();
		//    while (itemIterator.hasNext()) {
		//      defaultValueToLoad = defaultValueToLoad + this.constantManager.getConstant((String)itemIterator.next()) + " ";
		//    }
		defaultValueToLoad = defaultValueToLoad + this.fieldController.getDefaultValue();

		UserDomain userDomain = (UserDomain)this.fieldController.getDomain();
		this.comboField.setSelectedItem(userDomain.resolve(defaultValueToLoad));
		validate();
	}

	public String getVisibleFieldName()
	{
		return this.visibleFieldName;
	}

	public String getForeignField() {
		return this.foreignField; }

	public HashMap getDependencyValues() {
		return this.dependencyValues;
	}

	public Collection getSecondaryFields() {
		return this.secondaryFields;
	}

	public void setDependencyValues(LinkedHashMap dependencyValues, HashMap dependencyDomain) {
		UserDomain newDomain = new UserDomain("dependencyMasterDomain", dependencyDomain);
		this.fieldController.setDomain(newDomain);

		this.domainKeys = newDomain.getKeys();
		ArrayList domainValues = newDomain.getValues();

		this.dependencyValues = dependencyValues;
		this.comboField.removeAllItems();

		Iterator itemIterator = dependencyValues.keySet().iterator();

		while (itemIterator.hasNext()) {
			this.comboField.addItem(dependencyDomain.get(itemIterator.next()));
		}
	}

	private class ComboFieldChangeAction
	implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			if (DependencyMasterField.this.comboField.getItemCount() <= 0 ||
					DependencyMasterField.this.comboField.getSelectedIndex() < 0) {
				return;
			}
			String selectedValue = DependencyMasterField.this.getIndexKey(DependencyMasterField.this.comboField.getSelectedIndex());
			HashMap values = (HashMap)DependencyMasterField.this.dependencyValues.get(selectedValue);
			try {
				DependencyMasterField.this.dependency.updateSlaveFields(values);
			} catch (Exception e) {
				System.out.println("Ocurrio algún error inesperado en el combo selector de la dependencia...");
				e.printStackTrace();
			}

			setValue(selectedValue);
			fillField();
		}
	}
}
