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


/**
 * Complex Group.
 * 
 * The is the class used for storing Complex Group data.
 */
public class ComplexGroup extends SubGroup {

	/**
	 * SubGroups contained by this SubGroup.
	 */
	ArrayList<SubGroup> containedGroups = new ArrayList<SubGroup>();

	/**
	 * SubGroup Adder.
	 * 
	 * @param subGroup the subGroup we want to add.
	 */
	public void addSubGroup(SubGroup subGroup) {
		containedGroups.add(subGroup);
	}

	/**
	 * SubGroups Getter.
	 * 
	 * @return The set SubGroups (as an ArrayList).
	 */
	public ArrayList<SubGroup> getSubGroups() {
		return containedGroups;
	}

	/**
	 * SubGroups Setter.
	 * 
	 * @param subGroups the SubGroups (as an ArrayList) we want to set.
	 */
	public void setSubGroups(ArrayList<SubGroup> subGroups) {
		this.containedGroups = subGroups;
	}
}
