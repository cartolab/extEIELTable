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
