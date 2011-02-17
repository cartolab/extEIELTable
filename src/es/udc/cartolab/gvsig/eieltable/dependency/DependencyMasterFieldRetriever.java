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

package es.udc.cartolab.gvsig.eieltable.dependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import es.udc.cartolab.gvsig.eieltable.field.FieldController;
import es.udc.cartolab.gvsig.eieltable.util.FormsDAO;

public class DependencyMasterFieldRetriever
{
	private FormsDAO formsDAO;

	public DependencyMasterFieldRetriever()
	{
		this.formsDAO = new FormsDAO();
	}

	@SuppressWarnings("unchecked")
	public void updateMasterFields(Dependency dependency, HashMap valoresCampos)
	{
		try
		{
			HashMap applicationDAOCondition = new HashMap();

			if (dependency.getDependencyMasterField() != null)
			{
				ArrayList masterFieldNames = new ArrayList(dependency.getDependencyMasterField().getSecondaryFields());

				String masterFieldMainFieldName = dependency.getDependencyMasterField().getField().getName();

				masterFieldNames.add(masterFieldMainFieldName);
				String visibleField = dependency.getDependencyMasterField().getVisibleFieldName();

				ArrayList dependencyForeignKey = dependency.getForeignKey();

				ArrayList dependencyFields = dependency.getFields();
				ArrayList dependencyFieldsNames = new ArrayList();

				for (int i = 0; i < dependencyFields.size(); ++i) {
					FieldController oneDependencyField = (FieldController)dependencyFields.get(i);

					if (oneDependencyField.getName().compareTo(masterFieldMainFieldName) == 0) {
						if (!(dependencyFieldsNames.contains(dependency.getDependencyMasterField().getForeignField()))) {
							dependencyFieldsNames.add(dependency.getDependencyMasterField().getForeignField());
						}
					}
					else if (!(dependencyFieldsNames.contains(oneDependencyField.getName()))) {
						dependencyFieldsNames.add(oneDependencyField.getName());
					}

					if ((!(dependencyForeignKey.contains(oneDependencyField.getName()))) || ((masterFieldNames.contains(oneDependencyField.getName())) && (oneDependencyField.getIsConstant() != true)) ||
							(oneDependencyField.getName().compareTo(masterFieldMainFieldName) == 0)) {
						continue;
					}
					//          if ((valoresCampos.get(oneDependencyField.getName()) == null) && (oneDependencyField.getIsConstant() == true))
					//            applicationDAOCondition.put(oneDependencyField.getName(), "");
					//          else {
					applicationDAOCondition.put(oneDependencyField.getName(), valoresCampos.get(oneDependencyField.getName()));
					//          }

				}

				for (int i = 0; i < masterFieldNames.size(); ++i) {
					if (((String)masterFieldNames.get(i)).compareTo(masterFieldMainFieldName) == 0) {
						if (!(dependencyFieldsNames.contains(dependency.getDependencyMasterField().getForeignField()))) {
							dependencyFieldsNames.add(dependency.getDependencyMasterField().getForeignField());
						}
					}
					else if (!(dependencyFieldsNames.contains(masterFieldNames.get(i)))) {
						dependencyFieldsNames.add(masterFieldNames.get(i));
					}
				}

				dependencyFieldsNames.add(visibleField);

				ArrayList allDependencyPosibleValues = this.formsDAO.getFieldsCollection(applicationDAOCondition, dependency.getDataBase(), dependency.getTable(), dependencyFieldsNames);

				HashMap dependencyRowValues = new HashMap();
				LinkedHashMap dependencyDomainValues = new LinkedHashMap();
				LinkedHashMap dependencyValuesHashMap = new LinkedHashMap();

				for (int i = 0; i < allDependencyPosibleValues.size(); ++i) {
					dependencyRowValues = (HashMap)allDependencyPosibleValues.get(i);

					String masterFieldKey = "";
					for (int j = 0; j < masterFieldNames.size(); ++j) {
						if (((String)masterFieldNames.get(j)).compareTo(masterFieldMainFieldName) == 0) {
							masterFieldKey = masterFieldKey + dependencyRowValues.get(dependency.getDependencyMasterField().getForeignField()) + " ";
						} else {
							masterFieldKey = masterFieldKey + dependencyRowValues.get(masterFieldNames.get(j)) + " ";
						}
					}
					masterFieldKey = masterFieldKey.substring(0, masterFieldKey.length() - 1);

					dependencyDomainValues.put(masterFieldKey, dependencyRowValues.get(dependency.getDependencyMasterField().getVisibleFieldName()));
					dependencyValuesHashMap.put(masterFieldKey, dependencyRowValues);
				}

				dependency.getDependencyMasterField().setDependencyValues(dependencyValuesHashMap, dependencyDomainValues);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateFields(Dependency dependency, HashMap valoresCampos)
	{
		for (int i = 0; i < dependency.getFields().size(); ++i) {
			;
		}
	}

	public List<String> getDependencyFieldsNames(Dependency dependency) {
		ArrayList dependencyFieldsNames = new ArrayList();

		if (dependency.getDependencyMasterField() != null)
		{
			ArrayList masterFieldNames = new ArrayList(dependency.getDependencyMasterField().getSecondaryFields());

			String masterFieldMainFieldName = dependency.getDependencyMasterField().getField().getName();

			masterFieldNames.add(masterFieldMainFieldName);
			String visibleField = dependency.getDependencyMasterField().getVisibleFieldName();

			ArrayList dependencyForeignKey = dependency.getForeignKey();

			ArrayList dependencyFields = dependency.getFields();

			for (int i = 0; i < dependencyFields.size(); ++i) {
				FieldController oneDependencyField = (FieldController)dependencyFields.get(i);

				if (oneDependencyField.getName().compareTo(masterFieldMainFieldName) == 0) {
					if (!(dependencyFieldsNames.contains(dependency.getDependencyMasterField().getForeignField()))) {
						dependencyFieldsNames.add(dependency.getDependencyMasterField().getForeignField());
					}
				}
				else if (!(dependencyFieldsNames.contains(oneDependencyField.getName()))) {
					dependencyFieldsNames.add(oneDependencyField.getName());
				}

			}

			for (int i = 0; i < masterFieldNames.size(); ++i) {
				if (((String)masterFieldNames.get(i)).compareTo(masterFieldMainFieldName) == 0) {
					if (!(dependencyFieldsNames.contains(dependency.getDependencyMasterField().getForeignField()))) {
						dependencyFieldsNames.add(dependency.getDependencyMasterField().getForeignField());
					}
				}
				else if (!(dependencyFieldsNames.contains(masterFieldNames.get(i)))) {
					dependencyFieldsNames.add(masterFieldNames.get(i));
				}
			}

		}

		return dependencyFieldsNames;
	}
}
