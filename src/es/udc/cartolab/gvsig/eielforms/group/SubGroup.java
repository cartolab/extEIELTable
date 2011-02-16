package es.udc.cartolab.gvsig.eielforms.group;
import java.util.ArrayList;

import es.udc.cartolab.gvsig.eielforms.structure.SingleField;



/**
 * SubGroup.
 * 
 * The is the class used for storing second level Group data.
 * SubGroups are the Groups which store the real data, and Groups
 * only map the first level ones.
 * 
 * @see Group
 */
public abstract class SubGroup {

	/**
	 * The layout of this Group.
	 */
	protected String layout;
	/**
	 * The name of this Group.
	 */
	protected String name;
	/**
	 * The Single Fields of this Group.
	 */
	protected ArrayList<SingleField> fields = new ArrayList<SingleField>();

	/**
	 * Layout Getter.
	 * 
	 * @return The set layout.
	 */
	public String getLayout() {
		return layout;
	}

	/**
	 * Layout Setter.
	 * 
	 * @param layout the layout we want to set.
	 */
	public void setLayout(String layout) {
		this.layout = layout;
	}

	/**
	 * Name Getter.
	 * 
	 * @return The set name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Name Setter.
	 * 
	 * @param name the name we want to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Fields Getter.
	 * 
	 * @return The set fields (as an ArrayList).
	 */
	public ArrayList<SingleField> getFields() {
		return fields;
	}

	/**
	 * Fields Setter.
	 * 
	 * @param fields the fields (as an ArrayList) we want to set.
	 */
	public void setFields(ArrayList<SingleField> fields) {
		this.fields = fields;
	}

	/**
	 * Field Adder.
	 * 
	 * @param field the field we want to add.
	 */
	public void addField(SingleField field) {
		this.fields.add(field);
	}

}
