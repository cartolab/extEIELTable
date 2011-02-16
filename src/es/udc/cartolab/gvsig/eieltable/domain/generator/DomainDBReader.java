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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.udc.cartolab.gvsig.eieltable.formgenerator.FormException;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class DomainDBReader extends DomainReader {

	private String aplicationSchemaName = "eiel_aplicaciones";

	public DomainDBReader() {

	}

	public DomainDBReader(String applicationSchemaName) {
		this.aplicationSchemaName = applicationSchemaName;
	}

	public String getDomainDefinition(String domainName) throws FormException
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Connection connection = null;

		String queryString = "select definicion from \"" + this.aplicationSchemaName + "\".dominios ";
		queryString = queryString + "where nombre = ?";
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

				if (resultSet.next())
					domainXMLDefinition = resultSet.getString(1);
				else {
					domainXMLDefinition = null;
				}

			}
			catch (Exception e)
			{
				throw new FormException(e);
			} finally {
				if (preparedStatement!=null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						throw new FormException(e);
					}
				}
				if (resultSet != null) {
					try {
						resultSet.close();
					} catch (SQLException e) {
						throw new FormException(e);
					}
				}
			}
		} else { 
			throw new FormException("Sesión no iniciada");
		}

		return domainXMLDefinition;
	}

}
