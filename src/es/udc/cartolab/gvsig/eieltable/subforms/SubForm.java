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

package es.udc.cartolab.gvsig.eieltable.subforms;

import java.awt.GridBagConstraints;
import java.util.ArrayList;

import javax.swing.JPanel;

import es.udc.cartolab.gvsig.eieltable.field.FieldInterface;

public class SubForm
{
	private String table;
	private String dataBase;
	private String name;
	private ArrayList foreignKey;
	private FieldInterface primaryField;
	private ArrayList primaryKeys;
	private ArrayList fields;
	private JPanel panel;
	private GridBagConstraints gridbagconst;
	private String layout = "FLOWLAYOUT";

	public SubForm(String name, String table, String dataBase, ArrayList foreignKey, FieldInterface primaryField, ArrayList fields)
	{
		this.name = name;
		this.table = table;
		this.dataBase = dataBase;
		this.foreignKey = foreignKey;
		this.primaryField = primaryField;
		this.fields = new ArrayList();
		this.panel = new JPanel();
	}

	public ArrayList getPrimaryKeys()
	{
		return this.primaryKeys;
	}

	public void setPrimaryKeys(ArrayList primaryKeys)
	{
		this.primaryKeys = primaryKeys;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public FieldInterface getPrimaryField()
	{
		return this.primaryField;
	}

	public ArrayList getFields()
	{
		return this.fields;
	}

	public void addField(FieldInterface fieldInterface) {
		this.fields.add(fieldInterface);
		if (this.layout.compareTo("FLOWLAYOUT") == 0) {
			this.panel.add(fieldInterface.getLabel());
			this.panel.add(fieldInterface.getComponent());
		} else {
			this.gridbagconst.gridx = 0;
			this.panel.add(fieldInterface.getLabel(), this.gridbagconst);
			this.gridbagconst.gridx = 1;
			this.panel.add(fieldInterface.getComponent(), this.gridbagconst);
			updateLayout();
		}
	}

	private void updateLayout()
	{
		if (this.layout.compareTo("FLOWLAYOUT") == 0) {
			return;
		}
		if (this.layout.compareTo("GRIDBAGLAYOUT") == 0) {
			this.gridbagconst.gridy += 1;
		}
	}
}
