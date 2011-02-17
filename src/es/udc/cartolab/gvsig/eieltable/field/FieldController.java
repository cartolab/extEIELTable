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

package es.udc.cartolab.gvsig.eieltable.field;


import es.udc.cartolab.gvsig.eieltable.domain.Domain;

public class FieldController
{
	private String label;
	private String name;
	private Domain domain;
	private String defaultValue;
	private boolean editable;
	private boolean required;
	private boolean isKey;
	private boolean constantValue;
	private boolean isOrden;
	private String value;
	private String memoryValue;
	private boolean save;
	private String oldValue;
	private boolean isLength;
	private boolean isArea;
	private String unitArea;
	private String unitLength;

	public FieldController(String label, String name, Domain domain, String defaultValue, boolean editable, boolean required, boolean isKey, boolean constantValue, boolean isOrden)
	{
		this.label = label;
		this.name = name;
		this.domain = domain;
		this.defaultValue = defaultValue;
		this.value = defaultValue;
		this.editable = editable;
		this.required = required;
		this.isKey = isKey;
		this.constantValue = constantValue;
		this.memoryValue = "";
		this.oldValue = "";
		this.isOrden = isOrden;
	}

	public String getLabel() {
		return this.label;
	}

	public String getName() {
		return this.name;
	}

	public Domain getDomain() {
		return this.domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getDefaultValue()
	{
		return this.defaultValue;
	}

	public boolean getEditable() {
		return this.editable;
	}

	public boolean getRequired() {
		return this.required;
	}

	public boolean getIsKey() {
		return this.isKey;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getMemoryValue() {
		return this.memoryValue;
	}

	public void setMemoryValue(String value) {
		this.memoryValue = value;
	}

	public void setConstantValue(boolean value) {
		this.constantValue = value;
	}

	public boolean getIsConstant() {
		return this.constantValue;
	}

	public boolean isOrden() {
		return this.isOrden;
	}

	public boolean validate()
	{
		boolean isValid1 = false;
		boolean isValid2 = false;

		if (this.value == null) {
			this.value = "";
		}
		if (this.value.length() == 0) {
			if (this.required == true) {
				isValid1 = false;
			} else {
				isValid1 = true;
			}
		}
		else {
			isValid2 = this.domain.validate(this.value);
		}

		boolean isValid = isValid1 || isValid2;

		if (!isValid) {
			System.out.println("El campo " + this.label + " con domain " + this.domain.getName() + " de tipo " + this.domain.getType() + ", no es valido!!!");
		}

		return isValid;
	}

	public boolean mustSave() {
		return save;
	}

	public void setMustSave(boolean save) {
		this.save = save;
	}

	public FieldController clonar()
	{
		FieldController fieldController = new FieldController(this.label, this.name, this.domain, this.defaultValue, this.editable, this.required, this.isKey, this.constantValue, this.isOrden);
		fieldController.setValue(getValue());
		fieldController.setMemoryValue(getMemoryValue());
		if (fieldController.getDefaultValue().equals("")) {
			fieldController.setDefaultValue(getValue());
		}
		return fieldController;
	}

	/**
	 * Sets the current value of this field on DB (it should be only used when retrieving data from DB or after saving it).
	 * @param value
	 */
	public void setOldValue(String value) {
		oldValue = value;
	}

	/**
	 * Gets the current value of this field on DB.
	 * @return
	 */
	public String getOldValue() {
		return oldValue;
	}

	public void setIsLength(boolean bool_isLength, String unit) {
		isLength = bool_isLength;
		unitLength = unit;
	}

	public boolean isLength() {
		return isLength;
	}

	public void setIsArea(boolean bool_isArea, String unit) {
		isArea = bool_isArea;
		unitArea = unit;
	}

	public boolean isArea() {
		return isArea;
	}

	public String getUnitLength() {
		return unitLength;
	}

	public String getUnitArea() {
		return unitArea;
	}


}
