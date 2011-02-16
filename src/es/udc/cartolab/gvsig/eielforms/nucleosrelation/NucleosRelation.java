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

package es.udc.cartolab.gvsig.eielforms.nucleosrelation;

import java.util.HashMap;
import java.util.Map;

public class NucleosRelation {

	private HashMap<String, String> fields;
	private String tableName;

	public NucleosRelation(String tableName) {
		this.tableName = tableName;
		this.fields = new HashMap<String, String>();
	}

	public String getTableName() {
		return tableName;
	}

	public void addField(String tableName, String relationName) {
		fields.put(tableName, relationName);
	}


	public Map<String, String> getFields() {
		return fields;
	}

}
