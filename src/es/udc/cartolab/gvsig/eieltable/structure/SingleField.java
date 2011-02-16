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
