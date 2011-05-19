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

package es.udc.cartolab.gvsig.eieltable.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
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

public class AlphanumericTableForm extends JPanel implements IWindow, ActionListener {

	protected String formName;
	private WindowInfo viewInfo;
	private JPanel southPanel;
	protected FormController form;
	protected HashMap<String, String> key = new HashMap<String,String>();
	private JButton saveButton, newButton, closeButton, delButton;


	public AlphanumericTableForm(String formName) {
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

	private TableFrame getCenterTable() {
		FormDBReader formDAO = new FormDBReader();
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
			newButton = new JButton(PluginServices.getText(this, "eielTable_newRegisterButton"));
			newButton.addActionListener(this);
			southPanel.add(newButton);
			delButton = new JButton(PluginServices.getText(this, "eielTable_deleteRegisterButton"));
			delButton.addActionListener(this);
			southPanel.add(delButton);
			saveButton = new JButton(PluginServices.getText(this, "eielTable_saveButton"));
			saveButton.addActionListener(this);
			southPanel.add(saveButton);
			closeButton = new JButton(PluginServices.getText(this, "eielTable_closeButton"));
			closeButton.addActionListener(this);
			southPanel.add(closeButton);
			southPanel.setMinimumSize(southPanel.getPreferredSize());
			southPanel.setMaximumSize(southPanel.getPreferredSize());
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
		if (edit) {
			key.put(fieldName, fieldValue);
		} else {
			TableFrame table = (TableFrame) this.getComponent(0);
			table.voidTableDirectly();
		}

		return edit;


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

	private void saveModifiedRows() {

		TableFrame table = (TableFrame) this.getComponent(0);

		ArrayList<ArrayList<HashMap<String,Object>>> modified = table.getModifiedRows();
		HashMap<Integer,HashMap<String,Object>> created = table.getNewRows();
		ArrayList<HashMap<String,Object>> deleted = table.getDeletedRows();

		HashMap<Integer,HashMap<String,Object>> newValues = form.updateRows(modified, created, deleted);

		table.setSavedValues(newValues);

		table.clearPendingChanges();

	}

	private boolean hasUnsavedChanges() {

		TableFrame table = (TableFrame) this.getComponent(0);

		return ((table.getModifiedRows().size() +
			 table.getNewRows().size() +
			 table.getDeletedRows().size()) > 0);
	}

	private void createNewRow() {

		TableFrame table = (TableFrame) this.getComponent(0);

		table.createRow();

	}

	private void deleteRows() {

		TableFrame table = (TableFrame) this.getComponent(0);

		table.deleteSelectedRows();

	}

	protected void showWarning() {
		boolean changed = hasUnsavedChanges();
		if (changed) {
		    Object[] options = {
			    PluginServices.getText(this, "eielTable_saveButtonTooltip"),
			    PluginServices.getText(this, "eielTable_ignoreButton"),
			    PluginServices.getText(this, "eielTable_cancelButton") };
		    int response = JOptionPane.showOptionDialog(this,
			    PluginServices.getText(this, "eielTable_unsavedDataMessage"),
			    PluginServices.getText(this, "eielTable_unsavedDataTitle"),
			    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
			    null, // do not use a custom Icon
			    options, // the titles of buttons
			    options[1]); // default button title
		    switch (response){
			    case 0:
				    saveModifiedRows();
			    case 1:
				    close();
				    break;
			    case 2:
		    }
		} else close();
	}


	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() == saveButton) {
			this.saveModifiedRows();
		}

		if (arg0.getSource() == newButton) {
			this.createNewRow();
		}

		if (arg0.getSource() == delButton) {
			this.deleteRows();
			this.repaint();
		}

		if (arg0.getSource() == closeButton) {
			showWarning();
		}
	}
}
