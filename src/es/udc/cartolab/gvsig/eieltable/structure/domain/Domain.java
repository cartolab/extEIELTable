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

package es.udc.cartolab.gvsig.eieltable.structure.domain;

/**
 * Domain.
 * 
 * The base class Domain used for all data types.
 */
public abstract class Domain
{
	/**
	 * The name of the Domain.
	 */
	protected String name;
	/**
	 * Domain type.
	 */
	protected String tipo;
	/**
	 * A description of the Domain.
	 */
	protected String description;

	/**
	 * Domain constructor.
	 * 
	 * The constructor for Domain, which accepts its name and Domain type (as a String).
	 *
	 * @param name the name of the Domain.
	 * @param tipo the Domain type.
	 * 
	 * @return A new Domain.
	 */
	public Domain(String name, String tipo)
	{
		this.name = name;
		this.tipo = tipo;
	}

	/**
	 * Name Getter.
	 * 
	 * @return Name of the Domain.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Type Getter.
	 * 
	 * @return Type of the Domain.
	 */
	public String getType() {
		return this.tipo;
	}

	@Override
	public String toString() {
		return new String(this.description);
	}

	/**
	 * Validator.
	 * 
	 * Method for checking that a value is a valid one for this Domain.
	 *
	 * @param valor the value we want to check (as a String).
	 * 
	 * @return A boolean which tells us whether the value is valid or not.
	 */
	public abstract boolean validate(String paramString);
}
