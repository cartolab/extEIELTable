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

package es.udc.cartolab.gvsig.eieltable.domain.restriction;


/**
 * Restriction.
 * 
 * Class used for defining restrictions which can be applied to Domains
 * in order to restrict their values.
 * 
 */
public abstract class Restriction2
{
	/**
	 * The name of the Restriction.
	 */
	private String name;

	/**
	 * Restriction Constructor.
	 * 
	 * Constructor which accepts a name for the Restriction.
	 * 
	 * @param name the name we want for the Restriction.
	 * 
	 * @return a new Restriction.
	 */
	public Restriction2(String name)
	{
		this.name = name;
	}

	/**
	 * Validator.
	 * 
	 * Function used for checking the Restriction onto a value.
	 * 
	 * @param paramString the value we want to check the Restriction onto.
	 * 
	 * @return A boolean which says whether the value fulfills the Restriction
	 * or it doesn't.
	 */
	public abstract boolean validate(String paramString);

	@Override
	public String toString() {
		return new String("Restriction " + this.name);
	}
}
