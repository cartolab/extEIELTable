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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.udc.cartolab.gvsig.eielforms.structure.domain.Domain;


/**
 * Domain Database Reader.
 * 
 * Class used for retrieving domain definitions from DB.
 * 
 * @see Domain
 * @see DomainReader2
 */
public class DomainDBReader2 extends DomainReader2 {

	/**
	 * Domain Database Schema.
	 * 
	 * The name of the schema where we have the domains stored.
	 * It has a default value, but it can also be defined through
	 * the constructor.
	 */
	private String applicationSchemaName = "eiel_aplicaciones";

	/**
	 * Domain Database Reader Constructor.
	 * 
	 * Constructor which accepts no arguments.
	 * 
	 * @returns A new DomainDBReader.
	 */
	public DomainDBReader2() {

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
	public DomainDBReader2(String applicationSchemaName) {
		this.applicationSchemaName = applicationSchemaName;
	}

	@Override
	/**
	 * Domain Definition Retriever.
	 * 
	 * Method for retrieving a domain definition from database.
	 * The definition is returned as a String with its xml.
	 * 
	 * @param domainName the name of the domain we want to retrieve.
	 * 
	 * @throws FormException
	 * @returns The definition as a String with its xml.
	 */
	public String getDomainDefinition(String domainName) throws DomainException2
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Connection connection = null;

		String queryString = "select definicion from \"" + this.applicationSchemaName + "\".dominios ";
		queryString = queryString + "where nombre = ?";
		String domainXMLDefinition;
		//if (dbs != null) {
		try
		{

			//connection = dbs.getJavaConnection();

			connection = DBAux2.getConnection();

			preparedStatement = connection.prepareStatement(queryString);

			int i = 1;
			preparedStatement.setString(i++, domainName);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				domainXMLDefinition = resultSet.getString(1);
			} else {
				domainXMLDefinition = null;
			}

		}
		catch (Exception e)
		{
			throw new DomainException2(e);
		} finally {
			if (preparedStatement!=null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					throw new DomainException2(e);
				}
			}
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					throw new DomainException2(e);
				}
			}
		}
		/*} else {
			throw new FormException("Sesión no iniciada");
		}*/

		return domainXMLDefinition;
	}

}
