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

package es.udc.cartolab.gvsig.eielforms.dependency;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.border.TitledBorder;

import es.udc.cartolab.gvsig.eielforms.field.ComboFieldInterface;
import es.udc.cartolab.gvsig.eielforms.field.FieldController;
import es.udc.cartolab.gvsig.eielforms.field.FieldInterface;
import es.udc.cartolab.gvsig.eielforms.field.TextFieldInterface;
import es.udc.cartolab.gvsig.eielforms.groups.SingleFieldGroup;

public class Dependency extends SingleFieldGroup
{
	private String table;
	private String dataBase;
	private ArrayList foreignKey;
	private ArrayList ownFields;
	private ArrayList ownFieldsInterface;
	private String masterFieldName;
	private DependencyMasterField dependencyMasterField;
	private boolean knowOwnFields;
	private boolean knowOwnFieldsInterface;
	private String groupName;
	private String layout;
	private ArrayList<DependencyListener> listeners;

	public Dependency(String groupName, String layout, String table, String dataBase, ArrayList foreignKey)
	{
		super(groupName, layout);
		this.groupName = groupName;
		this.layout = layout;
		this.table = table;
		this.dataBase = dataBase;
		this.foreignKey = foreignKey;
		this.knowOwnFields = false;
		this.knowOwnFieldsInterface = false;
		this.ownFields = new ArrayList();
		this.ownFieldsInterface = new ArrayList();
		this.dependencyMasterField = null;
		this.listeners = new ArrayList<DependencyListener>();

		getInterface().setBorder(new TitledBorder(groupName));
	}

	public void setMasterFieldName(String name) {
		this.masterFieldName = name;
	}

	public String getTable() {
		return this.table;
	}

	public String getDataBase() {
		return this.dataBase;
	}

	public ArrayList getForeignKey() {
		return this.foreignKey;
	}

	public void setOwnFields(ArrayList ownFields) {
		this.ownFields = ownFields;
	}

	public ArrayList getOwnFields()
	{
		ArrayList allFields = getFields();

		fillFields();
		if (!this.knowOwnFields) {
			for (int i = 0; i < allFields.size(); ++i) {
				String oneFieldName = ((FieldController)allFields.get(i)).getName();
				if (this.foreignKey.contains(oneFieldName) == true) {
					this.ownFields.add(allFields.get(i));
				}
			}
			this.knowOwnFields = true;
		}
		return this.ownFields;
	}

	public ArrayList getOwnFieldsInterface()
	{
		ArrayList allFields = getFieldsInterface();

		fillFields();
		if (!this.knowOwnFieldsInterface) {
			for (int i = 0; i < allFields.size(); ++i) {
				FieldController field = ((FieldInterface)allFields.get(i)).getField();
				String oneFieldName = field.getName();
				if (this.foreignKey.contains(oneFieldName) == true || field.mustSave()) {
					this.ownFieldsInterface.add(allFields.get(i));
				}
			}
			this.knowOwnFieldsInterface = true;
		}
		return this.ownFieldsInterface;
	}

	public void addMasterField(DependencyMasterField masterField)
	{
		if (this.dependencyMasterField == null) {
			this.dependencyMasterField = masterField;
			addField(masterField);
			this.masterFieldName = masterField.getField().getName();
			this.foreignKey.add(this.masterFieldName);
		} else {
			System.out.println("No puedes introducir otro campo Master en la dependencia!!!");
		}
	}

	public DependencyMasterField getDependencyMasterField() {
		return this.dependencyMasterField;
	}

	protected void updateSlaveFields(HashMap values) {
		ArrayList allFields = getFields();

		for (int i = 0; i < allFields.size(); ++i)
		{
			FieldController oneFieldController = (FieldController)allFields.get(i);
			if (oneFieldController.getDomain().getName().compareTo("dependencyMasterDomain") != 0) {
				oneFieldController.setValue((String)values.get(oneFieldController.getName()));
			}
		}

		loadData();
		fireDependencyChanged();
	}

	public Dependency clonar() {
		Dependency dependency = new Dependency(this.groupName, this.layout, this.table, this.dataBase, this.foreignKey);
		dependency.setMasterFieldName(this.masterFieldName);
		dependency.setOwnFields(getOwnFields());
		ArrayList listFields = new ArrayList();
		listFields = getFields();
		for (int i = 0; i < listFields.size() - 1; ++i) {
			FieldController fieldController = (FieldController)listFields.get(i);
			FieldInterface fieldInterface;
			if (fieldController.getDomain().getType().compareTo("usuario") == 0)
			{
				fieldInterface = new ComboFieldInterface(fieldController);
				fieldInterface.loadValue();
			}
			else {
				fieldInterface = new TextFieldInterface(fieldController);
				fieldInterface.loadValue();
			}
			dependency.addField(fieldInterface);
		}
		dependency.addMasterField(this.dependencyMasterField);

		return dependency;
	}

	public void addDependencyListener(DependencyListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removeDependencyListener(DependencyListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	private void fireDependencyChanged() {
		for (DependencyListener listener : listeners) {
			listener.dependencyChanged(this);
		}
	}

}
