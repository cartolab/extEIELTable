/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
 *
 * This file is part of extEIELForms
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

package es.udc.cartolab.gvsig.eieltable.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiFrame.MDIFrame;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

import es.udc.cartolab.gvsig.eieltable.Parser;
import es.udc.cartolab.gvsig.eieltable.dependency.Dependency;
import es.udc.cartolab.gvsig.eieltable.dependency.DependencyMasterField;
import es.udc.cartolab.gvsig.eieltable.field.FieldController;
import es.udc.cartolab.gvsig.eieltable.field.FieldInterface;
import es.udc.cartolab.gvsig.eieltable.formgenerator.FormDBReader;
import es.udc.cartolab.gvsig.eieltable.formgenerator.FormException;
import es.udc.cartolab.gvsig.eieltable.formgenerator.FormGenerator;
import es.udc.cartolab.gvsig.eieltable.forms.FormController;
import es.udc.cartolab.gvsig.eieltable.util.FormsDAO;
import es.udc.cartolab.gvsig.eielutils.constants.Constants;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class AlphanumericForm extends JPanel implements IWindow, ActionListener {

	protected String formName;
	private WindowInfo viewInfo;
	private JPanel southPanel;
	private JScrollPane centerPanel;
	protected FormController form;
	protected HashMap<String, String> key = new HashMap();
	private JButton saveButton, newButton, closeButton, delButton;


	public AlphanumericForm(String formName) {
		this.formName = formName;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setFocusCycleRoot(true);
	}


	public WindowInfo getWindowInfo() {
		if (viewInfo == null){
			viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
			viewInfo.setTitle("Alphanumeric form");
			Dimension dim = getPreferredSize();
			MDIFrame a = (MDIFrame) PluginServices.getMainFrame();
			int maxHeight =  a.getHeight()-175;
			int maxWidth =  a.getWidth()-15;

			int width,heigth = 0;
			if (dim.getHeight()> maxHeight){
				heigth = maxHeight;
			}else{
				heigth = new Double(dim.getHeight()).intValue();
			}
			if (dim.getWidth()> maxWidth){
				width = maxWidth;
			}else{
				width = new Double(dim.getWidth()).intValue();
			}
			viewInfo.setWidth(width+20);
			viewInfo.setHeight(heigth+15);
		}
		return viewInfo;
	}


	/**
	 * Adds a listener to the Primary Key field
	 * @throws FormException
	 */
	protected void addPKChangeListener() throws FormException {
		ArrayList keyFields = form.getKey();
		if (keyFields.size()!=1) {
			throw new FormException("Clave mal formada");
		}
		final String keyName = ((FieldController) keyFields.get(0)).getName();
		ArrayList fields = ((Dependency)form.getDependencies().get(0)).getFieldsInterface();
		//		ArrayList fields = form.getFieldsInterface();
		boolean found = false;
		for (int i=0; i<fields.size(); i++) {
			FieldInterface fi = (FieldInterface) fields.get(i);
			String fieldName = fi.getField().getName();
			if (fieldName.equals(keyName)) {
				if (fi.getComponent() instanceof JTextField) {
					final JTextField tf = (JTextField) fi.getComponent();
					tf.getDocument().addDocumentListener(new DocumentListener() {


						public void changedUpdate(DocumentEvent arg0) {
							key.put(keyName, tf.getText());

						}


						public void insertUpdate(DocumentEvent arg0) {
							key.put(keyName, tf.getText());
						}


						public void removeUpdate(DocumentEvent arg0) {
							key.put(keyName, tf.getText());
						}

					});
					found = true;
					break;
				} else {
					throw new FormException("El campo de la clave no es un textfield!");
				}

			}
		}
		if (!found) {
			throw new FormException("No se encontró el campo de la PK!");
		}
	}

	private JScrollPane getCenterPanel() throws FormException {
		if (centerPanel == null) {

			if (form == null) {
				FormGenerator fg = new FormGenerator();
				form = fg.createFormController(formName);
				addPKChangeListener();
			}
			centerPanel = new JScrollPane(form.getInterface());
			centerPanel.setBorder(new EmptyBorder(0,0,0,0));

		}
		return centerPanel;
	}

	private TableFrame getCenterTable() {
		FormDBReader formDAO = new FormDBReader();
		String xml = "";
		try {
			xml = formDAO.getFormDefinition(formName);
		} catch (FormException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (form == null) {
				FormGenerator fg = new FormGenerator();
				form = fg.createFormController(formName);
				addPKChangeListener();
			}
		} catch (Exception e) {}
		Object[][] data = form.getData();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			String formDef = formDAO.getFormDefinition(this.formName);
			InputSource source = new InputSource(new StringReader(formDef));
			Document doc = db.parse(source);
			TableFrame table = Parser.parseXmlDoc(doc, data);
			return table;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected JPanel getButtonsPanel() {
		if (southPanel == null) {
			southPanel = new JPanel();
			saveButton = new JButton("Guardar");
			saveButton.addActionListener(this);
			southPanel.add(saveButton);
			newButton = new JButton("Nuevo registro");
			newButton.addActionListener(this);
			southPanel.add(newButton);
			delButton = new JButton("Borrar registros");
			delButton.addActionListener(this);
			southPanel.add(delButton);
			closeButton = new JButton("Cerrar");
			closeButton.addActionListener(this);
			southPanel.add(closeButton);
		}
		return southPanel;
	}


	public Object getWindowProfile() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * It only works if the window has been initialized
	 */
	public void setHeight(int height) {
		if (viewInfo != null) {
			viewInfo.setHeight(height);
		}
	}

	/**
	 * It only works if the window has been initialized
	 */
	public void setWidth(int width) {
		if (viewInfo != null) {
			viewInfo.setWidth(width);
		}
	}

	public void open() {

		try {
			add(getCenterTable());
			//add(getCenterPanel());
			add(getButtonsPanel());
			if (setKey()) {
				fillValues();
			}

			PluginServices.getMDIManager().addCentredWindow(this);

			viewInfo.setTitle(form.getName());

			FieldInterface field = getFocusField();
			if (field != null) {
				field.getComponent().requestFocusInWindow();
			}

		} catch (FormException e) {
			System.out.println("Error en el formulario: " + e.getMessage());
		}
	}

	private FieldInterface getFocusField() {
		ArrayList fields = form.getFieldsInterface();
		boolean found = false;
		FieldInterface focusField = null;
		for (int i=0; i<fields.size(); i++) {
			FieldInterface field = (FieldInterface) fields.get(i);
			if (field.getField().getEditable()) {
				if (!found || field instanceof DependencyMasterField) {
					focusField = field;
					found = true;
				}
			}
		}
		return focusField;
	}

	/**
	 * To get first key of the table, only used in open.
	 */
	protected boolean setKey() throws FormException {
		ArrayList keyFields = form.getKey();
		key = new HashMap();

		if (keyFields.size()!=1) {
			throw new FormException("Clave primaria mal formada en el formulario");
		}
		FieldController fc = (FieldController) keyFields.get(0);
		String fieldName = fc.getName();
		String fieldValue = getFirstKey(fieldName);
		boolean edit = fieldValue != null;
		enableEdit(edit);
		if (edit) {
			key.put(fieldName, fieldValue);
		}

		return edit;


	}

	private String getWhereClause() throws SQLException {
		DBSession dbs = DBSession.getCurrentSession();
		String where =" WHERE ";
		Constants c = Constants.getCurrentConstants();
		if (dbs!=null && c.constantsSelected()) {
			List<String> cols = Arrays.asList(dbs.getColumns(form.getDataBase(), form.getTable()));

			ArrayList<String> upperCols = new ArrayList<String>();
			for (String column : cols) {
				upperCols.add(column.toUpperCase());
			}

			ArrayList<String> constants = new ArrayList<String>();
			constants.add("FASE");
			constants.add("PROVINCIA");
			constants.add("MUNICIPIO");
			constants.add("ENTIDAD");
			constants.add("NUCLEO");

			int constn = 0;
			for (String constant : constants) {
				int pos = upperCols.indexOf(constant);
				if (pos>-1 && c.getValue(constant) != null) {
					constn++;
					where = where + cols.get(pos) + "='" + c.getValue(constant) + "' AND ";
				}
			}

			if (constn>0) {
				where = where.substring(0, where.length()-5);
			} else {
				where = "";
			}
		}
		return where;
	}


	private String getFirstKey(String keyName) throws FormException {

		FormsDAO fdao = new FormsDAO();
		ArrayList<String> fields = new ArrayList<String>();
		fields.add(keyName);
		HashMap<String, String> values = fdao.getKeyValues(key, form.getDataBase(), form.getTable(), fields);
		return values.get(keyName);

	}

	private void enableEdit(boolean enable) {
		if (saveButton!=null) {
			saveButton.setEnabled(enable);
		}
	}

	public void fillValues() {

		form.fillForm(key);
	}

	public void close() {
		PluginServices.getMDIManager().closeWindow(this);
	}

	private void checkModifiedRows() {
		TableFrame table = (TableFrame) this.getComponent(0);

		ArrayList<ArrayList<HashMap<String,Object>>> modified = table.getModifiedRows();

		Iterator<ArrayList<HashMap<String,Object>>> iter = modified.iterator();

		while(iter.hasNext()) {
			ArrayList<HashMap<String,Object>> row = iter.next();
			HashMap<String,Object> keys = row.get(0);
			HashMap<String,Object> values = row.get(1);
			Set<String> keysK = keys.keySet();
			Set<String> valuesK = values.keySet();

			System.out.println("Claves: ");
			Iterator<String> iterK = keysK.iterator();
			while(iterK.hasNext()) {
				String key = iterK.next();
				System.out.println(key + ": " + keys.get(key));
			}

			System.out.println("Valores: ");
			Iterator<String> iterV = valuesK.iterator();
			while(iterV.hasNext()) {
				String key = iterV.next();
				System.out.println(key + ": " + values.get(key));
			}
		}

	}

	private void saveModifiedRows() {

		TableFrame table = (TableFrame) this.getComponent(0);

		ArrayList<ArrayList<HashMap<String,Object>>> modified = table.getModifiedRows();
		ArrayList<HashMap<String,Object>> deleted = table.getDeletedRows();

		form.updateRows(modified, deleted);

		table.clearPendingChanges();

	}

	private void createNewRow() {

		TableFrame table = (TableFrame) this.getComponent(0);

		table.createRow();

	}

	private void deleteRows() {

		TableFrame table = (TableFrame) this.getComponent(0);

		table.deleteSelectedRows();

	}


	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() == saveButton) {
			this.saveModifiedRows();
		}

		if (arg0.getSource() == newButton) {
			this.createNewRow();
			/*EditAlphanumericForm eaf = new EditAlphanumericForm(this, formName);
			eaf.open();*/
		}

		if (arg0.getSource() == delButton) {
			this.deleteRows();
			this.repaint();
			/*EditAlphanumericForm eaf = new EditAlphanumericForm(this, formName);
			eaf.open();*/
		}

		if (arg0.getSource() == closeButton) {
			close();
		}
	}
}
