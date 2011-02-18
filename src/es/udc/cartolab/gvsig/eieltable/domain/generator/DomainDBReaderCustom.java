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

package es.udc.cartolab.gvsig.eieltable.domain.generator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.udc.cartolab.gvsig.eieltable.structure.domain.Domain;
import es.udc.cartolab.gvsig.users.utils.DBSession;


/**
 * Domain Database Reader.
 * 
 * Class used for retrieving domain definitions from DB.
 * 
 * @see Domain
 * @see DomainReaderCustom
 */
public class DomainDBReaderCustom extends DomainReaderCustom {

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
	public DomainDBReaderCustom() {

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
	public DomainDBReaderCustom(String applicationSchemaName) {
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
	public String getDomainDefinition(String domainName) throws DomainException
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Connection connection = null;

		String queryString = "select definicion from \"" + this.applicationSchemaName + "\".dominios ";
		queryString = queryString + "where initcap(nombre) = initcap( ? )";
		String domainXMLDefinition;
		DBSession dbs = DBSession.getCurrentSession();
		if (dbs != null) {
			try
			{

				connection = dbs.getJavaConnection();

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
				throw new DomainException(e);
			} finally {
				if (preparedStatement!=null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						throw new DomainException(e);
					}
				}
				if (resultSet != null) {
					try {
						resultSet.close();
					} catch (SQLException e) {
						throw new DomainException(e);
					}
				}
			}
		} else {
			throw new DomainException("Sesión no iniciada");
		}

		return domainXMLDefinition;
	}

}
