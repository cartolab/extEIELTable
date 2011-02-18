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

package es.udc.cartolab.gvsig.eieltable.structure.domain;


import java.util.ArrayList;

import es.udc.cartolab.gvsig.eieltable.domain.restriction.DecimalSizeRestrictionCustom;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.NumericFieldRestrictionCustom;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.RestrictionCustom;



/**
 * Basic Domain.
 * 
 * A Domain used for basic data types.
 */
public class BasicDomain extends Domain
{
	/**
	 * The base type of the Domain (as a String).
	 */
	private String tipoBase;
	/**
	 * All the restrictions appliable to this Domain.
	 */
	private ArrayList<RestrictionCustom> restrictions;

	/**
	 * Basic Domain constructor.
	 * 
	 * The constructor for Basic Domain, which accepts its name and base type (as a String).
	 *
	 * @param name the name of the Basic Domain.
	 * @param tipoBase the base type (by its name as a String) of the Basic Domain.
	 * 
	 * @return A new BasicDomain
	 */
	public BasicDomain(String name, String tipoBase)
	{
		super(name, "basico");
		this.tipoBase = tipoBase;
		this.restrictions = new ArrayList<RestrictionCustom>();
		//three tipoBase kinds: string, int, numeric (float?)
		if (this.tipoBase.equalsIgnoreCase("int") || this.tipoBase.equalsIgnoreCase("numeric")) {
			restrictions.add(new NumericFieldRestrictionCustom("tipoBase"));
			if (this.tipoBase.equalsIgnoreCase("int")) {
				//no decimal count allowed
				restrictions.add(new DecimalSizeRestrictionCustom("tipoBase", 0));
			}
		}
		this.description = new String(tipoBase);
	}

	@Override
	public boolean validate(String valor) {
		int i = 0;
		boolean valid = true;

		for (i = 0; i < this.restrictions.size(); ++i) {
			valid = (valid) && ((this.restrictions.get(i)).validate(valor));
		}
		return valid;
	}

	/**
	 * Restriction Adder.
	 * 
	 * Method for adding a new Restriction to this Domain.
	 *
	 * @param restriction the Restriction we want to add.
	 * 
	 * @see RestrictionCustom
	 */
	public void addRestriction(RestrictionCustom restriction)
	{
		this.restrictions.add(restriction);

		description = description + ", " + restriction.toString();
	}

	/**
	 * Restrictions Getter.
	 * 
	 * Method for getting all Restrictions added to this Domain.
	 *
	 * @return An ArrayList with all the Restrictions added to this Domain.
	 * 
	 * @see RestrictionCustom
	 */
	public ArrayList<RestrictionCustom> getRestrictions() {
		return this.restrictions;
	}


}
