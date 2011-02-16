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

package es.udc.cartolab.gvsig.eieltable.domain.generator;

import es.udc.cartolab.gvsig.eieltable.structure.domain.Domain;

/**
 * Domain Retrieval Exception.
 * 
 * Exception thrown when we have any problem
 * while retrieving or parsing a domain.
 * 
 * @see Domain
 * @see DomainReader2
 */
public class DomainException2 extends Exception {

	/**
	 * Domain Retrieval Exception Generic Exception Based Constructor.
	 * 
	 * Constructor which accepts a generic Exception
	 * for creating a Domain Exception based on it.
	 * 
	 * @param e the exception we want to create the DomainException based on.
	 * 
	 * @returns A new DomainException.
	 * 
	 * @see Exception
	 */
	public DomainException2(Exception e) {
		super(e);
	}

	/**
	 * Domain Retrieval Exception Constructor.
	 * 
	 * Constructor for creating a default Domain Exception.
	 * 
	 * @returns A new DomainException.
	 */
	public DomainException2() {
		super();
	}

	/**
	 * Domain Retrieval Exception Constructor.
	 * 
	 * Constructor which accepts an error message for
	 * creating a Domain Exception with it.
	 * 
	 * @param msg error message we want the Domain Exception to contain.
	 * 
	 * @returns A new DomainException.
	 */
	public DomainException2(String msg) {
		super(msg);
	}
}
