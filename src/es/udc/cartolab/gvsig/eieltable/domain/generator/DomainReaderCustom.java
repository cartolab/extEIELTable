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
import java.util.HashMap;

import es.udc.cartolab.gvsig.eieltable.structure.domain.Domain;
import es.udc.cartolab.gvsig.eieltable.structure.domain.UserDomain;
import es.udc.cartolab.gvsig.users.utils.DBSession;


/**
 * Domain Reader.
 * 
 * Class used for retrieving domain definitions.
 * 
 * @see Domain
 */
public abstract class DomainReaderCustom {

	/**
	 * Domain Definition Retriever.
	 * 
	 * Method for retrieving a domain definition.
	 * The definition should be returned as a String with its xml.
	 * 
	 * @param domainName the name of the domain we want to retrieve.
	 * 
	 * @throws DomainException
	 * @returns The definition as a String with its xml.
	 */
	public abstract String getDomainDefinition(String domainName) throws DomainException;


	/**
	 * Domain Retriever.
	 * 
	 * Method for retrieving a domain through its name and the schema
	 * and table it's stored in. A definition xml is retrieved and parsed
	 * for creating a Domain object.
	 * 
	 * @param domainName the name of the domain we want to retrieve.
	 * @param schema the schema the domain is stored in.
	 * @param table the table the domain is stored in.
	 * 
	 * @throws DomainException
	 * @returns The User Domain as specified in database.
	 */
	public UserDomain getDomain(String domainName, String schema, String table)
	throws DomainException
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Connection connection = null;

		HashMap values = new HashMap();

		String queryString = "select codigo, nombre from \"" + schema + "\"." + table;
		queryString = queryString + " order by nombre";
		DBSession dbs = DBSession.getCurrentSession();
		if (dbs != null) {
			try
			{
				connection = dbs.getJavaConnection();
				preparedStatement = connection.prepareStatement(queryString);

				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					values.put(resultSet.getString(1), resultSet.getString(2));
				}

				resultSet.close();
				preparedStatement.close();
				connection.close();
			}
			catch (Exception e)
			{
			}
			finally
			{
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
			throw new DomainException("No se ha iniciado sesion");
		}

		UserDomain domain = new UserDomain(domainName, values);
		return domain;

	}

}
