/*
 * Copyright (c) 2010. Cartolab (Universidade da Coru�a)
 *
 * This file is part of extEIELTable
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

package es.udc.cartolab.gvsig.eieltable.structure;
import java.util.ArrayList;


/**
 * Dependency.
 * 
 * Class used for storing dependencies data.
 */
public class Dependency {

	/**
	 * The DB table of the dependency.
	 */
	private String table;
	/**
	 * The DB of the dependency.
	 */
	private String dataBase;
	/**
	 * The layout of the dependency.
	 */
	private String layout;
	/**
	 * The DB table of the dependency.
	 */
	private ArrayList<SingleField> fields = new ArrayList<SingleField>();

	/**
	 * DB Getter.
	 * 
	 * @return The set DB.
	 */
	public String getDataBase() {
		return dataBase;
	}

	/**
	 * DB Setter.
	 * 
	 * @param dataBase the DB we want to set.
	 */
	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	/**
	 * Layout Getter.
	 * 
	 * @return The set layout.
	 */
	public String getLayout() {
		return layout;
	}

	/**
	 * Layout Setter.
	 * 
	 * @param layout the layout we want to set.
	 */
	public void setLayout(String layout) {
		this.layout = layout;
	}

	/**
	 * Table Getter.
	 * 
	 * @return The set table.
	 */
	public String getTable() {
		return table;
	}

	/**
	 * Table Setter.
	 * 
	 * @param table the DB table we want to set.
	 */
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * Fields Getter.
	 * 
	 * @return The set fields (as ArrayList).
	 */
	public ArrayList<SingleField> getFields() {
		return fields;
	}

	/**
	 * Fields Setter.
	 * 
	 * @param fields the fields (as an ArrayList) we want to set.
	 */
	public void setFields(ArrayList<SingleField> fields) {
		this.fields = fields;
	}

	/**
	 * Field Adder.
	 * 
	 * @param field the field we want to add.
	 */
	public void addField(SingleField field) {
		this.fields.add(field);
	}

}
