package es.udc.cartolab.gvsig.eielforms.group;

/**
 * Group.
 * 
 * The is the class used for storing Group data.
 * 
 * @see SubGroup
 */
public class Group {

	/**
	 * The SubGroup contained by this Group.
	 */
	private SubGroup subGroup;
	/**
	 * The layout of this Group.
	 */
	private String layout;
	/**
	 * The name of this Group.
	 */
	private String name;

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
	 * SubGroup Getter.
	 * 
	 * @return The set SubGroup.
	 */
	public SubGroup getSubGroup() {
		return subGroup;
	}

	/**
	 * SubGroup Setter.
	 * 
	 * @param subGroup the SubGroup we want to set.
	 */
	public void setSubGroup(SubGroup subGroup) {
		this.subGroup = subGroup;
	}

}
