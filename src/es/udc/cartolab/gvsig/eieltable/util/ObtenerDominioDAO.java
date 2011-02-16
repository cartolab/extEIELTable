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

package es.udc.cartolab.gvsig.eieltable.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JOptionPane;

import es.udc.cartolab.gvsig.eieltable.domain.UserDomain;
import es.udc.cartolab.gvsig.eieltable.formgenerator.FormException;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class ObtenerDominioDAO
{

	private DBSession dbs;

	public ObtenerDominioDAO() throws FormException {
		dbs = DBSession.getCurrentSession();
		if (dbs == null) {
			throw new FormException("Sesión no iniciada");
		}
	}

	public Vector obtenerDominiosAsignados(String nombreCampo, String dominio, String database, String tabla, ForeignKeyVO claveForanea) throws FormException
	{
		Vector dominiosAsignados = new Vector();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Connection connection = null;

		String queryString = "select " + nombreCampo + " from \"" + database + "\"." + tabla + "";
		Vector campos = claveForanea.getCampos();
		for (int i = 0; i < campos.size(); ++i) {
			CampoVO campo = (CampoVO)campos.get(i);
			if (i == 0) {
				queryString = queryString + " where " + campo.getNombre() + "='" + campo.getValor() + "'";
			} else {
				queryString = queryString + " and " + campo.getNombre() + "='" + campo.getValor() + "'";
			}
		}

		queryString = queryString + ";";
		try
		{

			connection = dbs.getJavaConnection();

			preparedStatement = connection.prepareStatement(queryString);

			int i = 1;
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				dominiosAsignados.add(resultSet.getString(1));
			}
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Ha ocurrido un error al recurperar los dominios asignados \n" + e.getMessage(), "Aviso", 2);

			e.printStackTrace();
		}
		finally {
			try {
				closeResultSet(resultSet);
				closeStatement(preparedStatement);
			} catch (FormException e) {
				throw new FormException(e);
			}
		}
		return dominiosAsignados;
	}

	public void borrarDatos(String database, String tabla, ForeignKeyVO claveForanea) throws FormException
	{
		Vector dominiosAsignados = new Vector();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Connection connection = null;

		String queryString = "delete  from \"" + database + "\"." + tabla + "";
		Vector campos = claveForanea.getCampos();
		for (int i = 0; i < campos.size(); ++i) {
			CampoVO campo = (CampoVO)campos.get(i);
			if (i == 0) {
				queryString = queryString + " where " + campo.getNombre() + "='" + campo.getValor() + "'";
			} else {
				queryString = queryString + " and " + campo.getNombre() + "='" + campo.getValor() + "'";
			}
		}

		queryString = queryString + ";";
		try
		{

			connection = dbs.getJavaConnection();

			preparedStatement = connection.prepareStatement(queryString);

			preparedStatement.executeUpdate();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Ha ocurrido un error al borrar los datos \n" + e.getMessage(), "Aviso", 2);

			e.printStackTrace();
		}
		finally {
			try {
				closeResultSet(resultSet);
				closeStatement(preparedStatement);
			} catch (FormException e) {
				throw new FormException(e);
			}
		}
	}

	public OtrosDatosVO obtenerDatosIniciales(UserDomain dominio, Vector campos, String nombreCampoPrimario, String valorCampoPrimario, String database, String tabla, ForeignKeyVO claveForanea) throws FormException
	{
		OtrosDatosVO dato = new OtrosDatosVO();
		Vector datos = new Vector();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Connection connection = null;

		String queryString = "select ";

		for (int i = 0; i < campos.size(); ++i) {
			CampoVO campo = (CampoVO)campos.get(i);
			if (i == 0) {
				queryString = queryString + campo.getNombre();
			} else {
				queryString = queryString + "," + campo.getNombre();
			}

		}

		queryString = queryString + " from \"" + database + "\"." + tabla;

		Vector camposClave = claveForanea.getCampos();
		for (int i = 0; i < camposClave.size(); ++i) {
			CampoVO campo = (CampoVO)camposClave.get(i);
			if (i == 0) {
				queryString = queryString + " where " + campo.getNombre() + "='" + campo.getValor() + "'";
			} else {
				queryString = queryString + " and " + campo.getNombre() + "='" + campo.getValor() + "'";
			}

		}

		queryString = queryString + " and " + nombreCampoPrimario + "='" + dominio.unResolve(valorCampoPrimario) + "'";

		queryString = queryString + ";";
		try
		{

			connection = dbs.getJavaConnection();

			preparedStatement = connection.prepareStatement(queryString);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				dato.setCodigo(valorCampoPrimario);
				int indice = 1;
				for (int i = 0; i < campos.size(); ++i) {
					Object object = resultSet.getObject(indice);
					String valor = "";
					if (object != null) {
						valor = object.toString();
					}
					((CampoVO)campos.get(i)).setValor(valor);
					indice += 1;
				}
				dato.setDatos(campos);
			}
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Ha ocurrido un error al recurperar los dominios asignados \n" + e.getMessage(), "Aviso", 2);

			e.printStackTrace();
		}
		finally {
			try {
				closeResultSet(resultSet);
				closeStatement(preparedStatement);
			} catch (FormException e) {
				throw new FormException(e);
			}
		}
		return dato;
	}

	public void insertarDatos(UserDomain dominio, String nombreCampo, OtrosDatosVO datos, String database, String tabla, ForeignKeyVO claveForanea) throws FormException
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Connection connection = null;

		String queryString = "insert into \"" + database + "\"." + tabla + "( ";

		Vector camposClave = claveForanea.getCampos();
		for (int i = 0; i < camposClave.size(); ++i) {
			CampoVO campo = (CampoVO)camposClave.get(i);
			if (i == 0) {
				queryString = queryString + campo.getNombre();
			} else {
				queryString = queryString + "," + campo.getNombre();
			}
		}

		queryString = queryString + "," + nombreCampo;
		for (int i = 0; i < datos.getDatos().size(); ++i) {
			CampoVO campo = (CampoVO)datos.getDatos().get(i);
			if (!campo.getValor().equals("")) {
				queryString = queryString + "," + campo.getNombre();
			}

		}

		queryString = queryString + ") values (";

		Vector camposValor = claveForanea.getCampos();
		for (int i = 0; i < camposValor.size(); ++i) {
			CampoVO campo = (CampoVO)camposValor.get(i);
			if (i == 0) {
				queryString = queryString + "'" + campo.getValor() + "'";
			} else {
				queryString = queryString + ",'" + campo.getValor() + "'";
			}
		}

		queryString = queryString + ",'" + dominio.unResolve(datos.getCodigo()) + "'";
		for (int i = 0; i < datos.getDatos().size(); ++i) {
			CampoVO campo = (CampoVO)datos.getDatos().get(i);
			if (!campo.getValor().equals("") && campo.getValor().getClass().equals(String.class)) {
				queryString = queryString + ",'" + campo.getValor() + "'";
			}
			else if (!campo.getValor().equals("") && !campo.getValor().getClass().equals(String.class)) {
				queryString = queryString + "," + campo.getValor();
			}
		}
		queryString = queryString + ")";
		try
		{
			connection = dbs.getJavaConnection();

			preparedStatement = connection.prepareStatement(queryString);

			preparedStatement.executeUpdate();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Ha ocurrido un error al insertar datos \n" + e.getMessage(), "Aviso", 2);

			e.printStackTrace();
		}
		finally {
			try {
				closeResultSet(resultSet);
				closeStatement(preparedStatement);
			} catch (FormException e) {
				throw new FormException(e);
			}
		}
	}

	public Object obtenerValorCampo(UserDomain dominio, String itemSeleccionado, String nombreCampo, String campoPrimario, String database, String tabla, ForeignKeyVO claveForanea) throws FormException
	{
		Object resultado = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Connection connection = null;

		String queryString = "select " + nombreCampo + "  from \"" + database + "\"." + tabla + " where " + campoPrimario + "= '" + dominio.unResolve(itemSeleccionado) + "'";

		Vector camposClave = claveForanea.getCampos();
		for (int i = 0; i < camposClave.size(); ++i) {
			CampoVO campo = (CampoVO)camposClave.get(i);
			queryString = queryString + " and " + campo.getNombre() + "='" + campo.getValor() + "'";
		}

		queryString = queryString + ";";
		try
		{

			connection = dbs.getJavaConnection();

			preparedStatement = connection.prepareStatement(queryString);

			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				resultado = resultSet.getObject(1);
			}
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Ha ocurrido un error al recuperar los datos \n" + e.getMessage(), "Aviso", 2);

			e.printStackTrace();
		}
		finally {
			try {
				closeResultSet(resultSet);
				closeStatement(preparedStatement);
			} catch (FormException e) {
				throw new FormException(e);
			}
		}
		return resultado;
	}

	private void closeStatement(Statement statement) throws FormException {
		if (statement!=null) {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new FormException(e);
			}
		}
	}

	private void closeResultSet(ResultSet resultSet) throws FormException {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				throw new FormException(e);
			}
		}
	}
}
