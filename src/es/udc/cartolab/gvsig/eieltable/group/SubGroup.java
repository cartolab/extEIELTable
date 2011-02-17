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

package es.udc.cartolab.gvsig.eieltable.group;
import java.util.ArrayList;

import es.udc.cartolab.gvsig.eieltable.structure.SingleField;



/**
 * SubGroup.
 * 
 * The is the class used for storing second level Group data.
 * SubGroups are the Groups which store the real data, and Groups
 * only map the first level ones.
 * 
 * @see Group
 */
public abstract class SubGroup {

	/**
	 * The layout of this Group.
	 */
	protected String layout;
	/**
	 * The name of this Group.
	 */
	protected String name;
	/**
	 * The Single Fields of this Group.
	 */
	protected ArrayList<SingleField> fields = new ArrayList<SingleField>();

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
	 * Name Getter.
	 * 
	 * @return The set name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Name Setter.
	 * 
	 * @param name the name we want to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Fields Getter.
	 * 
	 * @return The set fields (as an ArrayList).
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
