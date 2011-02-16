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

package es.udc.cartolab.gvsig.eielforms.domain.generator;

import java.util.HashMap;

import es.udc.cartolab.gvsig.eielforms.structure.domain.Domain;


/**
 * Domain Cache.
 * 
 * Class used as a cache for previously retrieved domains.
 * 
 * @see Domain
 */
public class DomainCache2
{

	/**
	 * HashMap used for storing the cache.
	 */
	private HashMap cache;

	/**
	 * Domain Cache Constructor.
	 * 
	 * This is the public constructor for Domain Cache, which takes no arguments.
	 * 
	 * @return A new  DomainCache.
	 */
	public DomainCache2()
	{
		this.cache = new HashMap();
	}

	/**
	 * Cache domain adder.
	 * 
	 * This function adds a domain to the cache with the given name.
	 * 
	 * @param name the name we want to register the domain with.
	 * @param domain the domain we want to register.
	 */
	public void addDomain(String name, Domain domain) {
		this.cache.put(name, domain);
	}

	/**
	 * Cache domain retriever.
	 * 
	 * This function retrieves a domain from the cache with the given name.
	 * 
	 * @param name the name of the Domain we want to retrieve.
	 * 
	 * @return The Domain for the given name.
	 */
	public Domain getDomain(String name) {
		return ((Domain)this.cache.get(name));
	}
}
