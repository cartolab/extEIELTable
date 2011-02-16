package es.udc.cartolab.gvsig.eieltable.group;
import java.util.ArrayList;


/**
 * Complex Group.
 * 
 * The is the class used for storing Complex Group data.
 */
public class ComplexGroup extends SubGroup {

	/**
	 * SubGroups contained by this SubGroup.
	 */
	ArrayList<SubGroup> containedGroups = new ArrayList<SubGroup>();

	/**
	 * SubGroup Adder.
	 * 
	 * @param subGroup the subGroup we want to add.
	 */
	public void addSubGroup(SubGroup subGroup) {
		containedGroups.add(subGroup);
	}

	/**
	 * SubGroups Getter.
	 * 
	 * @return The set SubGroups (as an ArrayList).
	 */
	public ArrayList<SubGroup> getSubGroups() {
		return containedGroups;
	}

	/**
	 * SubGroups Setter.
	 * 
	 * @param subGroups the SubGroups (as an ArrayList) we want to set.
	 */
	public void setSubGroups(ArrayList<SubGroup> subGroups) {
		this.containedGroups = subGroups;
	}
}
