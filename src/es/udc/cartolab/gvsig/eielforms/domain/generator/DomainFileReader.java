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

import es.udc.cartolab.gvsig.eielforms.formgenerator.FormException;

public class DomainFileReader extends DomainReader
{

	private final static String path = "gvSIG"+File.separator+"extensiones"+File.separator+"es.udc.cartolab.gvsig.eielforms"
	+File.separator+"domains"+File.separator;

	private String pathToDomains;

	public DomainFileReader() {
		this(path);
	}

	public DomainFileReader(String pathToDomains) {
		this.pathToDomains = pathToDomains;
	}

	@Override
	public String getDomainDefinition(String domainName) throws FormException
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
			throw new FormException(e);
		} catch (IOException e) {
			throw new FormException(e);
		}
		return sb.toString();
	}

}
