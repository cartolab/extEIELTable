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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * User Domain.
 * 
 * A Domain used for custom user types.
 */
public class UserDomain extends Domain
{
	/**
	 * The customized values for this User Domain as a HashMap accessed with the keys.
	 */
	private HashMap<String,String> values;
	/**
	 * The keys for this User Domain as a HashMap accessed with the customized values.
	 */
	private HashMap<String,String> keys;
	/**
	 * The keys for this User Domain
	 */
	private Set<String> keySet;

	/**
	 * User Domain Constructor.
	 * 
	 * The constructor for User Domain, which accepts its name and values (as a HashMap).
	 *
	 * @param name the name of the User Domain.
	 * @param values the values which make up this User Domain.
	 * 
	 * @return A new UserDomain
	 */
	public UserDomain(String name, HashMap<String,String> values)
	{
		super(name, "usuario");

		this.values = values;
		this.keySet = values.keySet();

		Iterator<String> clavesIterator = this.keySet.iterator();
		this.description = new String(name + " (");

		this.keys = new HashMap<String,String>();
		String oneValue = new String();
		while (clavesIterator.hasNext())
		{
			oneValue = (String)clavesIterator.next();

			this.keys.put(values.get(oneValue), oneValue);
			description = description + oneValue + ", ";
		}

		int pos = description.lastIndexOf(',');
		String finalChar = ")";
		if (pos<0) {
			pos = description.length()-2;
			finalChar = "";
		}
		description = description.substring(0, pos) + finalChar;
	}

	@Override
	public boolean validate(String value)
	{
		boolean encontrado = false;

		Iterator<String> clavesIterator = this.keySet.iterator();
		while ((clavesIterator.hasNext()) && (!(encontrado))) {
			if (value.compareTo((String)clavesIterator.next()) != 0) {
				continue;
			} encontrado = true;
		}
		return encontrado;
	}

	/**
	 * Values Retriever.
	 * 
	 * The method used for retrieving all values with an ArrayList.
	 * 
	 * @return An ArrayList with all the values (Strings) of this Domain.
	 */
	public ArrayList<String> getValues() {
		ArrayList<String> valoresArrayList = new ArrayList<String>();
		Iterator<String> clavesIterator = this.keySet.iterator();
		while (clavesIterator.hasNext()) {
			valoresArrayList.add(this.values.get(clavesIterator.next()));
		}
		return valoresArrayList;
	}

	/**
	 * Keys Retriever.
	 * 
	 * The method used for retrieving all keys with an ArrayList.
	 * 
	 * @return An ArrayList with all the keys (Strings) of this Domain.
	 */
	public ArrayList<String> getKeys() {
		ArrayList<String> valoresArrayList = new ArrayList<String>();
		Iterator<String> clavesIterator = this.keySet.iterator();
		while (clavesIterator.hasNext()) {
			valoresArrayList.add(clavesIterator.next());
		}
		return valoresArrayList;
	}

	/**
	 * Key Resolver.
	 * 
	 * The method used for retrieving a value with its key
	 * 
	 * @param key the key whose related value we want to retrieve.
	 * 
	 * @return The value related to the passed key.
	 */
	public String resolve(String key) {
		return ((String)this.values.get(key));
	}

	/**
	 * Key Unresolver.
	 * 
	 * The method used for retrieving a key with its value
	 * 
	 * @param value the value whose related key we want to retrieve.
	 * 
	 * @return The key related to the passed value.
	 */
	public String unResolve(String value) {
		return ((String)this.keys.get(value));
	}
}
