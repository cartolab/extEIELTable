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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import es.udc.cartolab.gvsig.eielforms.structure.domain.Domain;


/**
 * Domain File Reader.
 * 
 * Class used for retrieving domain definitions from files.
 * 
 * @see Domain
 * @see DomainReader2
 */
public class DomainFileReader2 extends DomainReader2
{

	/**
	 * Domain Folder Path.
	 * 
	 * The path to the folder where we have all the domains stored.
	 * It has a default value, but it can also be defined through
	 * the constructor.
	 */
	private String pathToDomains = "gvSIG"+File.separator+"extensiones"+File.separator+"es.udc.cartolab.gvsig.eielforms"
	+File.separator+"domains"+File.separator;

	/**
	 * Domain Database Reader Constructor.
	 * 
	 * Constructor which accepts no arguments.
	 * 
	 * @returns A new DomainDBReader.
	 */
	public DomainFileReader2() {
	}

	/**
	 * Domain Database Reader Constructor.
	 * 
	 * Constructor which accepts the database schema name as argument.
	 * 
	 * @param applicationSchemaName the name of the schema where we stored
	 * the domains.
	 * 
	 * @returns A new DomainDBReader.
	 */
	public DomainFileReader2(String pathToDomains) {
		this.pathToDomains = pathToDomains;
	}

	@Override
	/**
	 * Domain Definition Retriever.
	 * 
	 * Method for retrieving a domain definition by passing its name.
	 * The definition is returned as a String with its xml.
	 * 
	 * @param domainName the name of the domain we want to retrieve.
	 * 
	 * @throws FormException
	 * @returns The definition as a String with its xml.
	 */
	public String getDomainDefinition(String domainName) throws DomainException2
	{
		File file = new File(pathToDomains  + domainName.toLowerCase() + ".xml");
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (FileNotFoundException e) {
			throw new DomainException2(e);
		} catch (IOException e) {
			throw new DomainException2(e);
		}
		return sb.toString();
	}

}
