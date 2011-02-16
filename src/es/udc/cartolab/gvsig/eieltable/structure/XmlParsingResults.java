package es.udc.cartolab.gvsig.eieltable.structure;
import java.util.ArrayList;

import es.udc.cartolab.gvsig.eieltable.group.Group;



/**
 * Single Field.
 * 
 * Class used for storing xml parsing results.
 */
public class XmlParsingResults {

	/**
	 * Configuration values parsed.
	 */
	private ConfigurationValues configuration;
	/**
	 * Dependencies parsed.
	 */
	private ArrayList<Dependency> dependencies = new ArrayList<Dependency>();
	/**
	 * Groups parsed.
	 */
	private ArrayList<Group> groups = new ArrayList<Group>();

	/**
	 * Configuration Values Getter.
	 * 
	 * @return The set Configuration Values.
	 */
	public ConfigurationValues getConfigurationValues() {
		return configuration;
	}

	/**
	 * Configuration Values Setter.
	 * 
	 * @param fields the fields (as an ArrayList) we want to set.
	 */
	public void setConfigurationValues(ConfigurationValues configuration) {
		this.configuration = configuration;
	}

	/**
	 * Dependencies Getter.
	 * 
	 * @return The set Dependencies (as an ArrayList).
	 */
	public ArrayList<Dependency> getDependencies() {
		return dependencies;
	}

	/**
	 * Dependencies Setter.
	 * 
	 * @param dependencies the dependencies we want to set.
	 */
	public void setDependencies(ArrayList<Dependency> dependencies) {
		this.dependencies = dependencies;
	}

	/**
	 * Dependency Adder.
	 * 
	 * @param dependency the dependency we want to add.
	 */
	public void addDependency(Dependency dependency) {
		this.dependencies.add(dependency);
	}

	/**
	 * Groups Getter.
	 * 
	 * @return The set Groups (as an ArrayList).
	 */
	public ArrayList<Group> getGroups() {
		return groups;
	}

	/**
	 * Groups Setter.
	 * 
	 * @param groups the groups we want to set.
	 */
	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}

	/**
	 * Groups Adder.
	 * 
	 * @param group the group we want to add.
	 */
	public void addGroups(Group group) {
		this.groups.add(group);
	}

}
