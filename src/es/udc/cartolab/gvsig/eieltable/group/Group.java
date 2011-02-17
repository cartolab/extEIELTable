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

/**
 * Group.
 * 
 * The is the class used for storing Group data.
 * 
 * @see SubGroup
 */
public class Group {

	/**
	 * The SubGroup contained by this Group.
	 */
	private SubGroup subGroup;
	/**
	 * The layout of this Group.
	 */
	private String layout;
	/**
	 * The name of this Group.
	 */
	private String name;

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
	 * SubGroup Getter.
	 * 
	 * @return The set SubGroup.
	 */
	public SubGroup getSubGroup() {
		return subGroup;
	}

	/**
	 * SubGroup Setter.
	 * 
	 * @param subGroup the SubGroup we want to set.
	 */
	public void setSubGroup(SubGroup subGroup) {
		this.subGroup = subGroup;
	}

}
