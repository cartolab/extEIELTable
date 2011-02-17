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
import es.udc.cartolab.gvsig.eieltable.structure.domain.Domain;


/**
 * Single Field.
 * 
 * Class used for storing single fields data.
 */
public class SingleField {

	/**
	 * The name of the field.
	 */
	private String name;
	/**
	 * The label of the field.
	 */
	private String label;
	/**
	 * The Domain of the field.
	 */
	private Domain domain;
	/**
	 * A boolean which tells us whether the field is a key or not.
	 */
	private boolean isKey;
	/**
	 * A boolean which tells us whether the field is editable or not.
	 */
	private boolean editable;
	/**
	 * A boolean which tells us whether the field is required or not.
	 */
	private boolean required;
	/**
	 * A boolean which tells us whether the field is ordered or not.
	 */
	private boolean isOrden;
	/**
	 * The default value of the field.
	 */
	private String defaultValue;

	/**
	 * Name setter.
	 * 
	 * @param name the name we want to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Name getter.
	 * 
	 * @return The set name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Label setter.
	 * 
	 * @param label the label we want to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Label getter.
	 * 
	 * @return The set label.
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Domain setter.
	 * 
	 * @param domain the Domain we want to set.
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * Domain getter.
	 * 
	 * @return The set Domain.
	 */
	public Domain getDomain() {
		return this.domain;
	}

	/**
	 * IsKey setter.
	 * 
	 * @param isKey the isKey boolean value we want to set.
	 */
	public void setIsKey(boolean isKey) {
		this.isKey = isKey;
	}

	/**
	 * IsKey getter.
	 * 
	 * @return The isKey value.
	 */
	public boolean getIsKey() {
		return this.isKey;
	}

	/**
	 * Editable setter.
	 * 
	 * @param editable the editable boolean value we want to set.
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * Editable getter.
	 * 
	 * @return The editable value.
	 */
	public boolean getEditable() {
		return this.editable;
	}

	/**
	 * Required setter.
	 * 
	 * @param required the required boolean value we want to set.
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * Required getter.
	 * 
	 * @return The required value.
	 */
	public boolean getRequired() {
		return this.required;
	}

	/**
	 * IsOrden setter.
	 * 
	 * @param isOrden the isOrden boolean value we want to set.
	 */
	public void setIsOrden(boolean isOrden) {
		this.isOrden = isOrden;
	}

	/**
	 * IsOrden getter.
	 * 
	 * @return The isOrden value.
	 */
	public boolean getIsOrden() {
		return this.isOrden;
	}

	/**
	 * Default Value setter.
	 * 
	 * @param defaultValue the default value we want to set.
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Default Value getter.
	 * 
	 * @return The default value.
	 */
	public String getDefaultValue() {
		return this.defaultValue;
	}

}
