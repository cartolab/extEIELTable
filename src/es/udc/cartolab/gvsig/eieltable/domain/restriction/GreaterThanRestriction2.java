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
 * Greater Than Restriction.
 * 
 * Restriction which checks whether the passed value
 * is higher (>) than a given number.
 * 
 */
public class GreaterThanRestriction2 extends NumericFieldRestriction2
{
	/**
	 * The minimum value.
	 */
	private Float myValue;

	/**
	 * Greater Than Restriction Constructor.
	 * 
	 * Constructor which accepts a name for the Restriction,
	 * and the decimal min value as a Float.
	 * 
	 * @param name the name we want for the Restriction.
	 * @param value the min value as a Float.
	 * 
	 * @return a new GreaterThanRestriction.
	 */
	public GreaterThanRestriction2(String name, Float value)
	{
		super(name);
		this.myValue = value;
	}

	@Override
	public boolean validate(String value) {
		boolean valido = true;
		try
		{
			Float valorAux = new Float(value);
			if (valorAux.compareTo(this.myValue) > 0) {
				valido = true;
			} else {
				valido = false;
			}
		}
		catch (NumberFormatException e)
		{
			valido = false;
		}
		return valido;
	}

	@Override
	public String toString() {
		return new String("El valor del campo debe ser mayor que " + this.myValue.toString());
	}
}
