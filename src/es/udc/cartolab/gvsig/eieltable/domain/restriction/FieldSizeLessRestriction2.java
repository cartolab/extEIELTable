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

package es.udc.cartolab.gvsig.eieltable.domain.restriction;

/**
 * Field Size Less Restriction.
 * 
 * Restriction which checks whether the passed value
 * is shorter (<) than a given length.
 * 
 */
public class FieldSizeLessRestriction2 extends Restriction2
{
	/**
	 * The maximum length.
	 */
	private Integer length;

	/**
	 * Field Size Less Restriction Constructor.
	 * 
	 * Constructor which accepts a name for the Restriction,
	 * and the maximum length as an Integer.
	 * 
	 * @param name the name we want for the Restriction.
	 * @param length the maximum length as an Integer.
	 * 
	 * @return a new FieldSizeLessRestriction.
	 */
	public FieldSizeLessRestriction2(String name, Integer length)
	{
		super(name);
		this.length = length;
	}

	@Override
	public boolean validate(String value) {
		return (value.length() < this.length.intValue());
	}

	@Override
	public String toString() {
		return new String("Longitud de campo menor que " + this.length.toString());
	}
}
