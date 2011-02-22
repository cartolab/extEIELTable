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

package es.udc.cartolab.gvsig.eieltable.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.iver.cit.gvsig.fmap.drivers.DBException;

import es.udc.cartolab.gvsig.eieltable.formgenerator.FormException;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormsDAO {

	public FormsDAO() {
	}

	public List<HashMap<String, String>> getValues(HashMap whereFields, String schemaName, String table, List<String> fields) throws FormException {
		ResultSet resultSet = null;
		Connection connection = null;
		Statement statement = null;
		String condition = getWhereCondition(whereFields);
		String queryString = "SELECT ";
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < fields.size(); ++i) {
			queryString = queryString + fields.get(i) + ", ";
		}
		queryString = queryString.substring(0, queryString.length() - 2) + " ";

		if (!schemaName.equals("")) {
			queryString = queryString + "FROM \"" + schemaName + "\"." + table + " " + condition;
		} else {
			queryString = queryString + "FROM " + table + " " + condition;
		}
		System.out.println("Ejecutando la consulta >>>>>>>>> \n" + queryString);
		try
		{
			DBSession dbs = DBSession.getCurrentSession();
			if (dbs!=null) {
				connection = dbs.getJavaConnection();
				statement = connection.createStatement();
				resultSet = statement.executeQuery(queryString);

				while (resultSet.next()) {
					HashMap<String, String> row = new HashMap<String, String>();
					for (int i = 0; i<fields.size() ; ++i) {
						row.put(fields.get(i), resultSet.getString(fields.get(i)));
					}
					result.add(row);
				}
			} else {
				throw new FormException("La sesion no se ha iniciado");
			}

			return result;
		} catch (SQLException e) {
			try {
				DBSession.reconnect();
			} catch (DBException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				throw new FormException(e);
			}
		} finally {
			closeResultSet(resultSet);
			closeStatement(statement);
		}
	}

	public Object[][] getAllValuesArray(String schemaName, String table, List<String> fields) throws FormException {
		ResultSet resultSet = null;
		Connection connection = null;
		Statement statement = null;
		String queryString = "SELECT ";
		ArrayList<Object[]> result = new ArrayList<Object[]>();

		for (int i = 0; i < fields.size(); ++i) {
			queryString = queryString + fields.get(i) + ", ";
		}
		queryString = queryString.substring(0, queryString.length() - 2) + " ";

		if (!schemaName.equals("")) {
			queryString = queryString + "FROM \"" + schemaName + "\"." + table;
		} else {
			queryString = queryString + "FROM " + table;
		}
		System.out.println("Ejecutando la consulta >>>>>>>>> \n" + queryString);
		try
		{
			DBSession dbs = DBSession.getCurrentSession();
			if (dbs!=null) {
				connection = dbs.getJavaConnection();
				statement = connection.createStatement();
				resultSet = statement.executeQuery(queryString);

				while (resultSet.next()) {
					ArrayList<Object> row = new ArrayList<Object>();
					for (int i = 0; i<fields.size() ; ++i) {
						row.add(resultSet.getString(fields.get(i)));
					}
					result.add(row.toArray());
				}
			} else {
				throw new FormException("La sesion no se ha iniciado");
			}

			return (Object[][]) result.toArray(new Object[0][0]);
		} catch (SQLException e) {
			try {
				DBSession.reconnect();
			} catch (DBException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				throw new FormException(e);
			}
		} finally {
			closeResultSet(resultSet);
			closeStatement(statement);
		}
	}


	public HashMap<String, String> getKeyValues(HashMap key, String schemaName, String table, List<String> fields) throws FormException {

		List<HashMap<String, String>> values = getValues(key, schemaName, table, fields);
		List<HashMap<String, String>> values2 = getValues(new HashMap(), schemaName, table, fields);
		if (values.size()==0) {
			return new HashMap<String, String>();
		} else {
			return values.get(0);
		}

	}

	public void updateEntity(HashMap key, String schemaName, String table, HashMap fields)
	throws FormException
	{
		Connection connection = null;
		Statement statement = null;

		String condition = getWhereCondition(key);
		Set fieldsSet = fields.keySet();
		Iterator fieldsIterator = fieldsSet.iterator();

		String updateString = "";
		if (!schemaName.equals("")) {
			updateString = "UPDATE \"" + schemaName + "\"." + table + " SET \n";
		} else {
			updateString = "UPDATE " + table + " SET \n";
		}
		while (fieldsIterator.hasNext()) {
			String oneField = (String)fieldsIterator.next();
			String oneFieldValue;
			if (fields.get(oneField) instanceof Boolean) {
				if ((Boolean)fields.get(oneField)) {
					oneFieldValue = "SI";
				} else {
					oneFieldValue = "NO";
				}
			} else {
				oneFieldValue = (String)fields.get(oneField);
			}
			if (oneFieldValue != null) {
				if (oneFieldValue.compareTo("") != 0) {
					updateString = updateString + oneField + " = '" + oneFieldValue + "',\n ";
				} else {
					updateString = updateString + oneField + " = null,\n ";
				}
			} else {
				updateString = updateString + oneField + " = null,\n ";
			}
		}

		updateString = updateString.substring(0, updateString.length() - 3);
		updateString = updateString + "\n " + condition;

		System.out.println("SENTENCIA UPDATE EJECUTADA >>>>>>>>> \n" + updateString);
		try
		{
			DBSession dbs = DBSession.getCurrentSession();
			if (dbs!= null) {
				connection = dbs.getJavaConnection();
				statement = connection.createStatement();

				statement.executeUpdate(updateString);
				connection.commit();
			} else {
				throw new FormException("La sesion no se ha iniciado");
			}
		}
		catch (Exception e)
		{
			throw new FormException(e);
		} finally {
			//	      closeResultSet(resultSet);
			closeStatement(statement);
		}
	}

	public void insertEntity(HashMap fields, String schemaName, String table)
	throws FormException
	{
		Connection connection = null;
		PreparedStatement statement = null;

		Set fieldsSet = fields.keySet();
		Iterator fieldsIterator = fieldsSet.iterator();

		String insertString = "";

		if (!schemaName.equals("")) {
			insertString = "INSERT INTO \"" + schemaName + "\"." + table + "\n";
		} else {
			insertString = "INSERT INTO " + table + "\n";
		}
		String fieldsString = "(";
		String valuesString = "(";

		while (fieldsIterator.hasNext()) {
			String oneField = (String)fieldsIterator.next();
			fieldsString = fieldsString + oneField + ", ";

			valuesString = valuesString + "?, ";
		}

		fieldsString = fieldsString.substring(0, fieldsString.length() - 2) + ") ";
		valuesString = valuesString.substring(0, valuesString.length() - 2) + ") ";

		insertString = insertString + fieldsString + "VALUES " + valuesString;

		System.out.println("SENTENCIA INSERT EJECUTADA >>>>>>>>> \n" + insertString);
		try
		{
			DBSession dbs = DBSession.getCurrentSession();
			if (dbs!=null) {
				connection = dbs.getJavaConnection();

				statement = connection.prepareStatement(insertString);

				fieldsIterator = fieldsSet.iterator();
				int i=1;
				while (fieldsIterator.hasNext()) {
					String oneField = (String)fieldsIterator.next();
					String oneFieldValue;
					if (fields.get(oneField) instanceof Boolean) {
						if ((Boolean)fields.get(oneField)) {
							oneFieldValue = "SI";
						} else {
							oneFieldValue = "NO";
						}
					} else {
						oneFieldValue = (String)fields.get(oneField);
					}
					if (oneFieldValue != null) {
						if (oneFieldValue.compareTo("") != 0) {
							statement.setObject(i, oneFieldValue);
						} else {
							statement.setObject(i, null);
						}
					} else {
						statement.setObject(i, null);
					}
					i++;
				}

				statement.executeUpdate();
				connection.commit();
			} else {
				throw new FormException("La sesion no se ha iniciado");
			}
		}
		catch (Exception e)
		{
			try {
				DBSession.reconnect();
			} catch (DBException e1) {
				throw new FormException(e1);
			}
			throw new FormException(e);
		} finally {
			closeStatement(statement);
		}
	}

	public int deleteEntity(HashMap key, String schemaName, String table)
	throws FormException
	{
		Connection connection = null;
		Statement statement = null;
		String condition = getWhereCondition(key);

		String deleteString = "";

		if (!schemaName.equals("")) {
			deleteString = "DELETE FROM \"" + schemaName + "\"." + table + "\n";
		} else {
			deleteString = "DELETE FROM " + table + "\n";
		}
		deleteString = deleteString + condition;
		try
		{
			DBSession dbs = DBSession.getCurrentSession();
			if (dbs != null) {
				connection = dbs.getJavaConnection();

				statement = connection.createStatement();
				int updatedRows = statement.executeUpdate(deleteString);
				connection.commit();

				System.out.println("SENTENCIA DELETE EJECUTADA >>>>>>>>> \n" + deleteString);

				int i = updatedRows;

				return i;
			} else {
				throw new FormException("La sesion no se ha iniciado");
			}
		}
		catch (Exception e)
		{
			try {
				DBSession.reconnect();
			} catch (DBException e1) {
				throw new FormException(e1);
			}
			throw new FormException(e);
		} finally {
			closeStatement(statement);
		}
	}

	public String getHighestValue(HashMap key, String schemaName, String table, String field) throws FormException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String condition = getWhereCondition(key);
		if (condition.equals("")) {
			condition = "WHERE ";
		} else {
			condition = condition + " AND ";
		}
		condition = condition + field + " IS NOT NULL";

		String queryString = "SELECT " + field + " ";

		if (!schemaName.equals("")) {
			queryString = queryString + "FROM \"" + schemaName + "\"." + table + " " + condition;
		} else {
			queryString = queryString + "FROM " + table + " " + condition;
		}

		queryString = queryString + " ORDER BY " + field + " DESC LIMIT 1";
		System.out.println("CONSULTA EJECUTADA >>>>>>>>> \n" + queryString);

		String highestValue = null;
		try
		{
			DBSession dbs = DBSession.getCurrentSession();
			if (dbs != null) {
				connection = dbs.getJavaConnection();
				statement = connection.createStatement();

				resultSet = statement.executeQuery(queryString);
				connection.commit();


				while (resultSet.next()) {
					highestValue = resultSet.getString(1);
				}
			} else {
				throw new FormException("La sesion no se ha iniciado");
			}
		}
		catch (Exception e)
		{
			try {
				DBSession.reconnect();
			} catch (DBException e1) {
				throw new FormException(e1);
			}
			throw new FormException(e);
		} finally {
			closeResultSet(resultSet);
			closeStatement(statement);
		}

		if (highestValue == null) {
			highestValue = "00";
		}
		return highestValue;
	}

	public ArrayList getFieldsCollection(HashMap key, String schemaName, String table, ArrayList fields)
	throws FormException
	{
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		ArrayList fieldsCollection = new ArrayList();
		String condition = getWhereCondition(key);

		String queryString = "SELECT ";

		for (int i = 0; i < fields.size(); ++i) {
			queryString = queryString + (String)fields.get(i);
			if (i == fields.size()-1) {
				queryString = queryString + " AS visiblefield ";
			} else {
				queryString = queryString + ", ";
			}
		}
		//		queryString = queryString.substring(0, queryString.length() - 2) + " ";

		if (!schemaName.equals("")) {
			queryString = queryString + "FROM \"" + schemaName + "\"." + table + " " + condition;
		} else {
			queryString = queryString + "FROM " + table + " " + condition;
		}

		queryString = queryString + " ORDER BY visiblefield";
		System.out.println("CONSULTA EJECUTADA >>>>>>>>> \n" + queryString);
		try
		{
			DBSession dbs = DBSession.getCurrentSession();
			if (dbs != null) {
				connection = dbs.getJavaConnection();
				statement = connection.createStatement();

				resultSet = statement.executeQuery(queryString);

				while (resultSet.next()) {
					HashMap result = new HashMap();
					for (int i = 0; i < fields.size(); ++i) {
						result.put(fields.get(i), resultSet.getString(i + 1));
					}
					fieldsCollection.add(result);
				}

				ArrayList localArrayList1 = fieldsCollection;

				return localArrayList1;
			} else {
				throw new FormException("La sesion no se ha iniciado");
			}
		}
		catch (Exception e)
		{
			try {
				DBSession.reconnect();
			} catch (DBException e1) {
				throw new FormException(e1);
			}
			throw new FormException(e);
		} finally {
			closeResultSet(resultSet);
			closeStatement(statement);
		}
	}

	private String getWhereCondition(HashMap key) {
		Set keySet = key.keySet();
		Iterator claveIterator = keySet.iterator();

		String condition = "";

		if (claveIterator.hasNext()) {
			condition = "WHERE ";
			while (claveIterator.hasNext()) {
				String oneKey = (String)claveIterator.next();
				condition = condition + oneKey + " = '" + key.get(oneKey) + "' and ";
			}
			condition = condition.substring(0, condition.length() - 5);
		}
		return condition;
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
