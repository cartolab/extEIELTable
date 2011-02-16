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

package es.udc.cartolab.gvsig.eielforms.formgenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormDBReader extends FormReader {

	private String aplicationSchemaName = "eiel_aplicaciones";

	public FormDBReader() {

	}

	public FormDBReader(String applicationSchemaName) {
		this.aplicationSchemaName = applicationSchemaName;
	}

	@Override
	public String getFormDefinition(String formName) throws FormException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Connection connection = null;

		String queryString = "select definicion from \"" + this.aplicationSchemaName + "\".formularios ";
		queryString = queryString + "where capa = ?";
		String formXMLDefinition = null;
		DBSession dbs = DBSession.getCurrentSession();
		if (dbs != null) {
			try
			{

				connection = dbs.getJavaConnection();

				preparedStatement = connection.prepareStatement(queryString);

				int i = 1;
				preparedStatement.setString(i++, formName);

				resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
					formXMLDefinition = resultSet.getString(1);
				}

			}
			catch (Exception e)
			{
				throw new FormException(e);
			}
			finally
			{
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

		if (formXMLDefinition == null) {
			throw new FormException("Formulario no encontrado");
		}


		return formXMLDefinition;
	}
}
