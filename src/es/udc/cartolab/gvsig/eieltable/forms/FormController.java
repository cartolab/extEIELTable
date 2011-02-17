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

package es.udc.cartolab.gvsig.eieltable.forms;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import es.udc.cartolab.gvsig.eieltable.dependency.Dependency;
import es.udc.cartolab.gvsig.eieltable.dependency.DependencyListener;
import es.udc.cartolab.gvsig.eieltable.dependency.DependencyMasterField;
import es.udc.cartolab.gvsig.eieltable.dependency.DependencyMasterFieldRetriever;
import es.udc.cartolab.gvsig.eieltable.domain.BasicDomain;
import es.udc.cartolab.gvsig.eieltable.domain.Domain;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.DecimalSizeRestriction;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.Restriction;
import es.udc.cartolab.gvsig.eieltable.field.FieldController;
import es.udc.cartolab.gvsig.eieltable.field.FieldInterface;
import es.udc.cartolab.gvsig.eieltable.field.listener.FieldChangeEvent;
import es.udc.cartolab.gvsig.eieltable.field.listener.FieldChangeListener;
import es.udc.cartolab.gvsig.eieltable.formgenerator.FormException;
import es.udc.cartolab.gvsig.eieltable.formgenerator.Subject;
import es.udc.cartolab.gvsig.eieltable.forms.listener.FormChangeEvent;
import es.udc.cartolab.gvsig.eieltable.forms.listener.FormChangeListener;
import es.udc.cartolab.gvsig.eieltable.groups.FieldGroup;
import es.udc.cartolab.gvsig.eieltable.nucleosrelation.NucleosRelation;
import es.udc.cartolab.gvsig.eieltable.subforms.SubForm;
import es.udc.cartolab.gvsig.eieltable.util.FormsDAO;
import es.udc.cartolab.gvsig.eielutils.constants.Constants;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormController extends Subject
{

	public enum Unit {

		M, KM, M2, KM2, NOVALUE;

		public static Unit toUnit(String unit) {


			try {
				return valueOf(unit.toUpperCase());
			}
			catch (Exception e) {
				return NOVALUE;
			}
		}

	}


	private boolean knowKey;
	private String dataBase;
	private String table;
	private String layer;
	private ArrayList key;
	private FormInterface formInterface;
	private String name;
	private DependencyMasterFieldRetriever dependencyMasterFieldRetriever;
	private ArrayList<FormChangeListener> listeners = new ArrayList<FormChangeListener>();
	private FormFieldListener fieldsListener;
	private String pollTable = "";
	private boolean pollButton = false;
	private PollDependencyListener pollListener;
	private HashMap<FieldController, OrderDependencyListener> orderListeners;
	DecimalFormat df;
	private double length;
	double area;

	public FormController(String layer, String dataBase, String table, String layout, String name, String title)
	{
		this.dependencyMasterFieldRetriever = new DependencyMasterFieldRetriever();
		this.layer = layer;
		this.dataBase = dataBase;
		this.table = table;
		this.name = name;

		this.df = new DecimalFormat("000");

		this.knowKey = false;
		this.key = new ArrayList();
		this.formInterface = new FormInterface(this, layout, title);

		this.fieldsListener = new FormFieldListener(this);

		this.pollListener = new PollDependencyListener();
		this.orderListeners = new HashMap<FieldController, OrderDependencyListener>();
	}

	public String getDataBase() {
		return this.dataBase;
	}

	public String getTable() {
		return this.table;
	}

	public FormInterface getInterface() {
		return this.formInterface;
	}

	public String getLayer() {
		return this.layer;
	}

	public String getName() {
		return this.name;
	}

	public void addGroup(FieldGroup grupo) {
		this.formInterface.addGroup(grupo);
	}

	public void addSubForm(SubForm subform) {
		this.formInterface.addSubFormulario(subform);
	}

	public ArrayList getGroups() {
		return this.formInterface.getGroups();
	}

	public void addDependency(Dependency dependency) {
		this.formInterface.addDependency(dependency);
	}

	public ArrayList getDependencies() {
		return this.formInterface.getDependencies();
	}

	public void addSubformsButton(ArrayList subforms)
	{
		this.formInterface.addSubForm(subforms);
	}

	public void addNucleosRelationButton(NucleosRelation relation) {
		this.formInterface.addNucleosRelationButton(relation);
	}

	//  public void setModal(boolean modal)
	//  {
	//    this.formInterface.setModal(modal);
	//  }

	public ArrayList getFields()
	{
		ArrayList grupos = getGroups();
		ArrayList dependencias = getDependencies();
		ArrayList temporal = new ArrayList();
		for (int i = 0; i < grupos.size(); ++i) {
			temporal.addAll(((FieldGroup)grupos.get(i)).getFields());
		}
		for (int i = 0; i < dependencias.size(); ++i) {
			temporal.addAll(((Dependency)dependencias.get(i)).getOwnFields());
		}
		return temporal;
	}


	public ArrayList getFieldsInterface()
	{
		ArrayList grupos = getGroups();
		ArrayList dependencias = getDependencies();
		ArrayList temporal = new ArrayList();

		for (int i = 0; i < grupos.size(); ++i) {
			temporal.addAll(((FieldGroup)grupos.get(i)).getFieldsInterface());
		}

		for (int i = 0; i < dependencias.size(); ++i) {
			temporal.addAll(((Dependency)dependencias.get(i)).getOwnFieldsInterface());
		}

		return temporal;
	}
	/**
	 * to be used only in getAllFieldValues
	 * @return
	 */
	private ArrayList getAllFieldsInterface() {

		ArrayList grupos = getGroups();
		ArrayList dependencias = getDependencies();
		ArrayList temporal = new ArrayList();

		for (int i = 0; i < grupos.size(); ++i) {
			temporal.addAll(((FieldGroup)grupos.get(i)).getFieldsInterface());
		}

		for (int i = 0; i < dependencias.size(); ++i) {
			temporal.addAll(((Dependency)dependencias.get(i)).getFieldsInterface());
		}

		return temporal;

	}

	public ArrayList getKey()
	{
		ArrayList grupos = getGroups();
		ArrayList dependencias = getDependencies();

		if (!this.knowKey) {
			for (int i = 0; i < grupos.size(); ++i) {
				this.key.addAll(((FieldGroup)grupos.get(i)).getKey());
			}
			for (int i = 0; i < dependencias.size(); ++i) {
				this.key.addAll(((Dependency)dependencias.get(i)).getKey());
			}
			this.knowKey = true;
		}
		return this.key;
	}

	public void showInterface(boolean visible) {
		this.formInterface.showInterface(visible);
	}

	private HashMap getEielKey(HashMap fields) {
		HashMap aux = new HashMap();

		ArrayList<String> keyFields = new ArrayList<String>();
		keyFields.add("fase");
		keyFields.add("provincia");
		keyFields.add("municipio");

		Set keySet = fields.keySet();
		Iterator iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String kStr = (String) iterator.next();
			if (keyFields.contains(kStr.toLowerCase())) {
				aux.put(kStr, fields.get(kStr));
			}
		}

		return aux;
	}

	private String getOrderValue(HashMap fields, FieldController oneField) {
		FormsDAO fdao = new FormsDAO();
		String value = "999";
		try {
			HashMap map = getEielKey(fields);
			String highestVal = fdao.getHighestValue(getEielKey(fields), dataBase, table, oneField.getName());
			Integer val = Integer.parseInt(highestVal) + 1;
			value = df.format(val);

		} catch (FormException e) {
			e.printStackTrace();
		}

		return value;
	}


	public void executeQuery(HashMap fields)
	{
		ArrayList groups = getGroups();
		for (int i = 0; i < groups.size(); ++i) {
			ArrayList groupOfFields = ((FieldGroup)groups.get(i)).getFields();

			for (int j = 0; j < groupOfFields.size(); ++j) {
				FieldController oneField = (FieldController)groupOfFields.get(j);
				String value = (String)fields.get(oneField.getName());
				if (oneField.isOrden()) {
					OrderDependencyListener orderListener = orderListeners.get(oneField);
					if (orderListener != null) {
						orderListener.setInitialValue(value);
					} else {
						FieldInterface oneFieldInterface = ((FieldGroup)groups.get(i)).getFieldInterface(oneField.getName());
						if (oneFieldInterface != null) {
							orderListeners.put(oneField, new OrderDependencyListener(value, oneFieldInterface));
						}
					}
				}
				if (value == null && oneField.isOrden()) {
					value = getOrderValue(fields, oneField);
				}
				if (value == null && oneField.isLength()) {
					value = getLengthValue(oneField.getUnitLength(), getDecimalSizeRestriction(oneField.getDomain()));
				}
				if (value == null && oneField.isArea()) {
					value = getAreaValue(oneField.getUnitArea(), getDecimalSizeRestriction(oneField.getDomain()));
				}
				if (value == null) {
					if (!oneField.getDefaultValue().equals("")) {
						value = oneField.getDefaultValue();
					}
				}
				oneField.setValue(value);
				oneField.setOldValue(value);
			}

		}


		ArrayList dependencies = getDependencies();
		for (int i = 0; i < dependencies.size(); ++i) {
			Dependency oneDependency = (Dependency)dependencies.get(i);

			//add listeners for order fields
			Set<FieldController> keySet = orderListeners.keySet();
			Iterator<FieldController> it = keySet.iterator();
			while (it.hasNext()) {
				OrderDependencyListener listener = orderListeners.get(it.next());
				oneDependency.addDependencyListener(listener);
			}
			ArrayList groupOfFields = oneDependency.getFields();

			if (oneDependency.getDependencyMasterField() != null)
			{
				for (int j = 0; j < groupOfFields.size(); ++j) {
					FieldController oneField = (FieldController)groupOfFields.get(j);

					if (oneField.getName().compareTo(oneDependency.getDependencyMasterField().getField().getName()) == 0) {
						oneField.setValue((String)fields.get(oneDependency.getName() + ".." + oneDependency.getDependencyMasterField().getForeignField()));
						oneField.setOldValue((String)fields.get(oneDependency.getName() + ".." + oneDependency.getDependencyMasterField().getForeignField()));
					} else {
						//						oneField.setValue((String)fields.get(oneDependency.getName() + ".." + oneField.getName()));
						oneField.setValue((String)fields.get(oneField.getName()));
						oneField.setOldValue((String)fields.get(oneField.getName()));
						if (oneField.getValue()==null) {
							oneField.setValue(oneField.getDefaultValue());
							oneField.setOldValue(oneField.getDefaultValue());
						}
						System.out.println("Value:" + oneField.getValue());
					}

				}

				DependencyMasterField oneDependencyMasterField = oneDependency.getDependencyMasterField();
				if (oneDependencyMasterField.getSecondaryFields().size() > 0) {
					String masterFieldKey = "";

					ArrayList masterFieldNames = new ArrayList(oneDependencyMasterField.getSecondaryFields());
					masterFieldNames.add(oneDependencyMasterField.getField().getName());

					for (int j = 0; j < masterFieldNames.size(); ++j) {
						masterFieldKey = masterFieldKey + (String)fields.get(new StringBuilder().append(oneDependency.getName()).append("..").append(masterFieldNames.get(j)).toString()) + " ";
					}
					masterFieldKey = masterFieldKey.substring(0, masterFieldKey.length() - 1);

					FieldController oneField = oneDependencyMasterField.getField();
					oneField.setValue(masterFieldKey);
					oneField.setOldValue(masterFieldKey);
				}

			}
			else
			{
				for (int j = 0; j < groupOfFields.size(); ++j) {
					FieldController oneField = (FieldController)groupOfFields.get(j);
					//					oneField.setValue((String)fields.get(oneDependency.getName() + ".." + oneField.getName()));
					oneField.setValue(oneField.getDefaultValue());
					oneField.setOldValue(oneField.getDefaultValue());
				}
			}
		}

		this.formInterface.loadData();
		this.formInterface.performQuery();
	}

	private int getDecimalSizeRestriction(Domain domain) {

		int numDec = 0;
		if (domain instanceof BasicDomain) {
			for (Restriction restriction : ((BasicDomain) domain).getRestrictions()) {
				if (restriction instanceof DecimalSizeRestriction) {
					numDec = ((DecimalSizeRestriction) restriction).getDecimalRestriction();
				}
			}
		}

		return numDec;

	}

	private String getLengthValue(String unit, int numDecimal) {

		double value;

		switch (Unit.toUnit(unit)) {
		case KM: value = length/1000.0;
		case M:
		default:
			value = length;
		}

		return round(value, numDecimal);

	}

	private String round(double value, int numDecimal) {
		if (numDecimal>0) {
			BigDecimal dec = new BigDecimal(value);
			dec = dec.setScale(numDecimal, BigDecimal.ROUND_HALF_UP);
			return Double.toString(dec.doubleValue());
		} else {
			return Long.toString(Math.round(value));
		}
	}

	private String getAreaValue(String unit, int numDecimal) {

		double value;

		switch (Unit.toUnit(unit)) {
		case KM2: value = area/1000000.0;
		case M2:
		default:
			value = area;
		}

		return round(value, numDecimal);
	}

	public void setLengthValue(double length) {
		this.length = length;
	}

	public void setAreaValue(double area) {
		this.area = area;
	}

	public void initFields() {
		this.formInterface.initFields();
	}

	public void startInsertion() {
		this.formInterface.startInsertion();
	}

	private HashMap<String, String> getFieldValues() {
		ArrayList<String> idFields = new ArrayList<String>();
		idFields.add("id");
		idFields.add("gid");
		ArrayList fieldsInterface = getFieldsInterface();
		HashMap<String, String> fieldValues = new HashMap<String, String>();
		for (int i=0; i< fieldsInterface.size(); i++) {
			FieldInterface fi = (FieldInterface) fieldsInterface.get(i);
			FieldController fc = fi.getField();
			String fieldName = fc.getName();
			if (!(fi instanceof DependencyMasterField) && !idFields.contains(fieldName.toLowerCase())) {
				fieldValues.put(fieldName, fc.getValue());
			}
		}
		return fieldValues;
	}

	/**
	 * To get all field values(including dependencies), only to be used in poll method
	 * @return
	 */
	private HashMap<String, String> getAllFieldValues() {


		ArrayList fieldsInterface = getAllFieldsInterface();
		HashMap<String, String> fieldValues = new HashMap<String, String>();
		for (int i=0; i< fieldsInterface.size(); i++) {
			FieldInterface fi = (FieldInterface) fieldsInterface.get(i);
			FieldController fc = fi.getField();
			String fieldName = fc.getName();
			if (!(fi instanceof DependencyMasterField)) {
				fieldValues.put(fieldName, fc.getValue());
			}
		}
		return fieldValues;

	}

	public void insert()
	{
		HashMap<String, String> fieldValues = getFieldValues();

		FormsDAO fdao = new FormsDAO();
		try {
			fdao.insertEntity(fieldValues, dataBase, table);
		} catch (FormException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//		this.formInterface.showInterface(true);

	}

	public boolean validate()
	{
		ArrayList grupos = getGroups();
		ArrayList dependencias = getDependencies();
		boolean esValido = true;

		for (int i = 0; i < grupos.size(); ++i) {
			esValido &= ((FieldGroup)grupos.get(i)).validate();
		}

		for (int i = 0; i < dependencias.size(); ++i) {
			esValido &= ((Dependency)dependencias.get(i)).validate();
		}

		//    this.formInterface.update();

		return esValido;
	}

	public Object[][] getData() {

		List<String> fieldNames = this.dependencyMasterFieldRetriever.getDependencyFieldsNames((Dependency)this.getDependencies().get(0));
		FormsDAO fdao = new FormsDAO();
		Object[][] map = new Object[0][0];
		try {
			map = fdao.getAllValuesArray(getDataBase(), getTable(), fieldNames);
		} catch (FormException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
	}

	public void fillForm(HashMap keys) {

		ArrayList fields = getFields();
		ArrayList<String> fieldNames = new ArrayList<String>();
		for (int i=0; i<fields.size(); i++){
			fieldNames.add(((FieldController) fields.get(i)).getName());
		}

		//	  ArrayList dep = getDependencies();
		//	  for (int i=0; i<dep.size(); i++) {
		//		  ArrayList depFields = ((Dependency)dep.get(i)).getFields();
		//		  for (int j=0; j<depFields.size(); j++) {
		//			  String fieldName = ((FieldController)depFields.get(j)).getName();
		//			  if (!fieldNames.contains(fieldName)) {
		//				  fieldNames.add(fieldName);
		//			  }
		//		  }
		//	  }
		FormsDAO fdao = new FormsDAO();
		HashMap map;
		try {
			map = fdao.getKeyValues(keys, getDataBase(), getTable(), fieldNames);

			map.putAll(getDependenciesFields(map));

			updateDependencyFields(map);
			executeQuery(map);
		} catch (FormException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HashMap<String, String> getFieldValues(List<String> fieldNames) {

		HashMap<String, String> fieldValues = new HashMap<String, String>();

		ArrayList fields = getAllFieldsInterface();
		for (int i=0; i<fields.size(); i++) {
			FieldInterface fi = (FieldInterface) fields.get(i);
			String name = fi.getField().getName();
			if (fieldNames.contains(name)) {
				if (!fieldValues.containsKey(name) || !(fi instanceof DependencyMasterField)) {
					fieldValues.put(name, fi.getField().getValue());
				}
			}
		}

		return fieldValues;

	}

	public void updateDependencyFields(HashMap fields) {
		ArrayList dependencies = getDependencies();

		for (int i = 0; i < dependencies.size(); ++i) {
			if (((Dependency)dependencies.get(i)).getDependencyMasterField() != null) {
				this.dependencyMasterFieldRetriever.updateMasterFields((Dependency)dependencies.get(i), fields);
			} else {
				this.dependencyMasterFieldRetriever.updateFields((Dependency)dependencies.get(i), fields);
			}
		}
	}

	private HashMap getDependenciesFields(HashMap formFields) {

		ArrayList dependencies = getDependencies();
		ArrayList<String> foreignKey = new ArrayList<String>();

		HashMap allDependenciesResult = new HashMap();

		try {
			for (int i=0; i< dependencies.size(); i++) {

				Dependency oneDependency = (Dependency) dependencies.get(i);
				HashMap dependencyFK = new HashMap();

				ArrayList<String> dependencyFieldNames = new ArrayList<String>();
				ArrayList<FieldController> dependencyFields = oneDependency.getFields();

				for (int j=0; j<dependencyFields.size(); j++) {
					String oneDependencyFieldName =  dependencyFields.get(j).getName();;
					DependencyMasterField dmf = oneDependency.getDependencyMasterField();
					if (dmf != null && oneDependencyFieldName.equals(dmf.getField().getName())) {
						oneDependencyFieldName = dmf.getForeignField();
					}
					dependencyFieldNames.add(oneDependencyFieldName);
				}

				foreignKey = oneDependency.getForeignKey();
				for (int j=0; j<foreignKey.size(); j++) {
					String oneForeignKeyField = foreignKey.get(j);
					String oneForeignKeyValue = (String) formFields.get(oneForeignKeyField);
					if (oneForeignKeyValue == null) {
						Constants cts = Constants.getCurrentConstants();
						String value = null;
						if (cts != null) {
							value = cts.getValue(oneForeignKeyField);
						}
						if (value != null) {
							oneForeignKeyValue = value;
						} else {
							oneForeignKeyValue = "";
						}
						formFields.put(oneForeignKeyField, oneForeignKeyValue);
					}
					dependencyFK.put(oneForeignKeyField, oneForeignKeyValue);
				}

				FormsDAO fdao = new FormsDAO();
				HashMap dependencyResult = fdao.getKeyValues(dependencyFK, oneDependency.getDataBase(), oneDependency.getTable(), dependencyFieldNames);
				Set fieldSet = dependencyResult.keySet();
				Iterator fieldsIterator = fieldSet.iterator();

				while (fieldsIterator.hasNext()) {
					String oneField = (String) fieldsIterator.next();
					allDependenciesResult.put(new String(oneDependency.getName() + ".." + oneField), dependencyResult.get(oneField));
				}

				dependencyResult = null;
				dependencyFK = null;


			}
			return allDependenciesResult;
		} catch (FormException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void update(HashMap key) {

		ArrayList fields = getFields();
		ArrayList fieldsInterface = getFieldsInterface();
		HashMap<String, String> fieldValues = new HashMap<String, String>();
		for (int i=0; i< fieldsInterface.size(); i++) {
			FieldInterface fi = (FieldInterface) fieldsInterface.get(i);
			FieldController fc = fi.getField();
			if (!(fi instanceof DependencyMasterField)) {
				fieldValues.put(fc.getName(), fc.getValue());
			}
			fc.setOldValue(fc.getValue());
		}

		FormsDAO fdao = new FormsDAO();
		try {
			fdao.updateEntity(key, dataBase, table, fieldValues);
		} catch (FormException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HashMap<Integer, HashMap<String,Object>> updateRows(ArrayList<ArrayList<HashMap<String,Object>>> modRows, HashMap<Integer, HashMap<String,Object>> newRows, ArrayList<HashMap<String,Object>> delRows) {

		HashMap<Integer,HashMap<String,Object>> newValues = new HashMap<Integer,HashMap<String,Object>>();
		ArrayList<HashMap<String,Object>> row;
		FormsDAO fdao = new FormsDAO();
		Iterator<ArrayList<HashMap<String,Object>>> iter = modRows.iterator();

		while (iter.hasNext()) {
			row = iter.next();
			try {
				if (row.get(0).size() > 0) {
					fdao.updateEntity(row.get(0), dataBase, table, row.get(1));
				}
			} catch (FormException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Set<Integer> keys = newRows.keySet();
		Iterator<Integer> iter2 = keys.iterator();
		HashMap<String,Object> values = new HashMap<String,Object>();
		int key;
		String newId;

		while (iter2.hasNext()) {
			key = iter2.next();
			try {
				values = newRows.get(key);
				fdao.insertEntity(values, dataBase, table);

				/*
				 * TODO We should try to create a more generic method,
				 * at least with the serial field name obtained through
				 * a config file.
				 * A better approach could obtain the serial field name
				 * from the DB itself.
				 */
				newId = fdao.getHighestValue(new HashMap(), dataBase, table, "gid");
				values.put("gid", newId);
				newValues.put(key, values);
			} catch (FormException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Iterator<HashMap<String,Object>> iter3 = delRows.iterator();

		while (iter3.hasNext()) {
			try {
				fdao.deleteEntity(iter3.next(), dataBase, table);
			} catch (FormException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return newValues;

	}

	public void addFormChangeListener(FormChangeListener listener) {

		if (!listeners.contains(listener)) {
			listeners.add(listener);
			ArrayList fields = getAllFieldsInterface();
			for (int i=0; i<fields.size(); i++) {
				if (fields.get(i) instanceof FieldInterface) {
					FieldInterface field = (FieldInterface) fields.get(i);
					field.addFieldChangeListener(fieldsListener);
				}
			}
		}

	}

	public void removeFormChangeListener(FormChangeListener listener) {

		if (listeners.contains(listener)) {
			listeners.remove(listener);
			if (listeners.size()==0) {
				ArrayList fields = getAllFieldsInterface();
				for (int i=0; i<fields.size(); i++) {
					if (fields.get(i) instanceof FieldInterface) {
						FieldInterface field = (FieldInterface) fields.get(i);
						field.removeFieldChangeListener(fieldsListener);
					}
				}
			}
		}

	}

	private class FormFieldListener implements FieldChangeListener {

		private FormController form;

		public FormFieldListener(FormController form) {
			this.form = form;
		}


		public void fieldChanged(FieldChangeEvent e) {
			FormChangeEvent event = new FormChangeEvent(form, e);
			for (FormChangeListener listener : listeners) {
				listener.formChanged(event);
			}
		}

	}

	public void addPollButton(String pollTable) {
		this.pollButton = true;
		this.pollTable = pollTable;
		this.formInterface.addPollButton();
		ArrayList dependencies = formInterface.getDependencies();
		for (int i=0; i<dependencies.size(); i++) {
			Dependency d = (Dependency) dependencies.get(i);
			d.addDependencyListener(pollListener);
		}
	}

	public void removePollButton() {
		this.pollButton = false;
		this.formInterface.removePollButton();
		ArrayList dependencies = formInterface.getDependencies();
		for (int i=0; i<dependencies.size(); i++) {
			Dependency d = (Dependency) dependencies.get(i);
			d.removeDependencyListener(pollListener);
		}
	}

	public boolean hasPollButton() {
		return this.pollButton;
	}

	public String getPollTable() {
		return this.pollTable;
	}

	public void poll() {
		if (this.pollButton) {
			DBSession dbs = DBSession.getCurrentSession();
			if (dbs!=null) {
				HashMap<String, String> fieldValues = getAllFieldValues();
				FormsDAO fdao = new FormsDAO();
				try {
					fdao.insertEntity(fieldValues, dataBase, pollTable);
				} catch (FormException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		formInterface.enablePollButton(false);
	}

	public boolean isPolled() {

		boolean polled = false;

		if (this.pollButton) {

			DBSession dbs = DBSession.getCurrentSession();
			if (dbs != null) {

				ArrayList<String> keyNames = new ArrayList<String>();
				for (int i=0; i<key.size();i++) {
					FieldController fc = (FieldController) key.get(i);
					keyNames.add(fc.getName());
				}

				HashMap<String, String> keyValues = getFieldValues(keyNames);

				FormsDAO fdao = new FormsDAO();
				try {
					HashMap<String, String> val = fdao.getKeyValues(keyValues, dbs.getSchema(), pollTable, keyNames);
					if (!val.isEmpty()) {
						polled = true;
					}
				} catch (FormException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return polled;
	}

	private class PollDependencyListener implements DependencyListener {

		public void dependencyChanged(Dependency dep) {
			formInterface.enablePollButton(!isPolled());
		}

	}

	private class OrderDependencyListener implements DependencyListener {

		private String initialValue;
		private FieldInterface orderField;

		public OrderDependencyListener(String initialValue, FieldInterface orderField) {
			this.initialValue = initialValue;
			this.orderField = orderField;
		}

		public void setInitialValue(String value) {
			initialValue = value;
		}

		public void dependencyChanged(Dependency dep) {
			HashMap<String, String> fields = getFieldValues();
			if (initialValue == null) {
				String value = getOrderValue(fields, orderField.getField());
				orderField.setValue(value);
				orderField.getField().setOldValue(value);
			}
		}

	}

	//  public void addEntitiesPanel(SelectEntityPanel selectEntityPanel) {
	//    this.formInterface.addEntitiesPanel(selectEntityPanel);
	//  }

	//  public void showEntitiesPanel(boolean visible) {
	//    this.formInterface.showEntitiesPanel(visible);
	//  }

	//  public void fillEntitiesPanel(Collection entityIds, Integer defaultEntity) {
	//    this.formInterface.fillEntitiesPanel(entityIds, defaultEntity);
	//  }
}
