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

package es.udc.cartolab.gvsig.eieltable.structure;

/**
 * Configuration Values.
 * 
 * Class used for storing configuration values data.
 */
public class ConfigurationValues {

	/**
	 * The configured DB.
	 */
	private String dataBase;
	/**
	 * The configured table.
	 */
	private String table;
	/**
	 * The configured layer.
	 */
	private String layer;

	/**
	 * DB getter.
	 * 
	 * @return The set DB.
	 */
	public String getDataBase() {
		return dataBase;
	}

	/**
	 * DB setter.
	 * 
	 * @param dataBase the DB we want to set.
	 */
	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	/**
	 * Layer getter.
	 * 
	 * @return The set layer.
	 */
	public String getLayer() {
		return layer;
	}

	/**
	 * Layer setter.
	 * 
	 * @param layer the layer we want to set.
	 */
	public void setLayer(String layer) {
		this.layer = layer;
	}

	/**
	 * Table getter.
	 * 
	 * @return The set table.
	 */
	public String getTable() {
		return table;
	}

	/**
	 * Table setter.
	 * 
	 * @param table the table we want to set.
	 */
	public void setTable(String table) {
		this.table = table;
	}

}
