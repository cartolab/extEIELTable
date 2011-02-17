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

public class NumericFieldRestriction extends Restriction
{
	public NumericFieldRestriction(String name)
	{
		super(name);
	}

	@Override
	public boolean validate(String value)
	{
		boolean valido = true;
		Float prueba;
		try
		{
			prueba = new Float(value);
		} catch (NumberFormatException e) {
			valido = false;
		}
		return valido;
	}

	@Override
	public String toString() {
		return new String("Campo numérico");
	}
}
