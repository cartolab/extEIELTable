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

package es.udc.cartolab.gvsig.eieltable.forms;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import es.udc.cartolab.gvsig.eieltable.dependency.Dependency;
import es.udc.cartolab.gvsig.eieltable.field.FieldInterface;
import es.udc.cartolab.gvsig.eieltable.forms.panel.EditPanel;
import es.udc.cartolab.gvsig.eieltable.forms.panel.InsertPanel;
import es.udc.cartolab.gvsig.eieltable.forms.panel.QueryPanel;
import es.udc.cartolab.gvsig.eieltable.forms.panel.SubFormButtonPanel;
import es.udc.cartolab.gvsig.eieltable.groups.FieldGroup;
import es.udc.cartolab.gvsig.eieltable.nucleosrelation.NucleosRelation;
import es.udc.cartolab.gvsig.eieltable.nucleosrelation.NucleosRelationButtonPanel;
import es.udc.cartolab.gvsig.eieltable.subforms.SubForm;
import es.udc.cartolab.gvsig.eieltable.subforms.SubFormController;
import es.udc.cartolab.gvsig.eieltable.subforms.SubFormInterface;

//public class FormInterface extends JDialog
public class FormInterface extends JPanel
{
	private String layout;
	private String title;
	private FormController formController;
	private ArrayList groups;
	private ArrayList dependencies;
	private ArrayList subforms;
	private JPanel panel;
	//  private JPanel titlePanel;
	private GridBagConstraints gridbagconst;
	private boolean visible;
	private QueryPanel queryPanel;
	private SubFormButtonPanel subFormPanel;
	private EditPanel editPanel;
	private InsertPanel insertPanel;
	private boolean hasSubform = false;
	private ArrayList subformsControllers;
	private JButton pollButton;
	private boolean hasPollButton = false;
	private NucleosRelation nucleosRelation = null;
	private NucleosRelationButtonPanel nucleosRelationPanel;
	//  private SelectEntityPanel selectEntityPanel;

	protected FormInterface(FormController formController, String layout, String title)
	{
		super();
		this.formController = formController;
		this.groups = new ArrayList();
		this.subforms = new ArrayList();
		this.subformsControllers = new ArrayList();
		this.dependencies = new ArrayList();
		this.layout = layout.toUpperCase();
		this.visible = false;
		this.pollButton = new JButton("Encuestar");
		setTitle(title);
		initFormInterface();
		configureLayout();

		setLocation(500, 100);
		//    setDefaultCloseOperation(1);
	}

	private void initFormInterface() {
		setLayout(new BorderLayout());

		//    this.titlePanel = new TitlePanel(this.formController.getName(), 25);

		this.panel = new JPanel();
		this.panel.setVisible(true);

		this.queryPanel = new QueryPanel(this);
		this.editPanel = new EditPanel(this);
		//
		this.subFormPanel = new SubFormButtonPanel(this);

		this.subFormPanel.setVisible(true);
		//
		this.insertPanel = new InsertPanel(this);

		//    this.mainPane.add(this.titlePanel, "North");
		add(this.panel, "Center");
	}

	//  protected void addEntitiesPanel(SelectEntityPanel selectEntityPanel)
	//  {
	//    this.selectEntityPanel = selectEntityPanel;
	//    this.mainPane.add(selectEntityPanel, "West");
	//  }

	protected void addGroup(FieldGroup group) {
		this.groups.add(group);
		this.panel.add(group.getInterface(), this.gridbagconst);
		updateLayout();
	}

	protected void addSubFormulario(SubForm subform) {
		this.subforms.add(subform);
	}

	protected void addDependency(Dependency dependency)
	{
		this.dependencies.add(dependency);
		this.panel.add(dependency.getInterface(), this.gridbagconst);
		updateLayout();
	}

	public void setNucleosRelation(NucleosRelation relation) {
		this.nucleosRelation = relation;
	}

	public ArrayList getGroups()
	{
		return this.groups;
	}

	public ArrayList getDependencies() {
		return this.dependencies; }

	public ArrayList getSubForms() {
		return this.subforms;
	}

	public ArrayList getSubformsControllers() {
		return this.subformsControllers;
	}

	public FormController getFormController() {
		return this.formController;
	}

	public void showInterface(boolean visible)
	{
		this.visible = visible;
		update();
	}

	private void configureLayout() {
		if (this.layout.compareTo("FLOWLAYOUT") == 0) {
			this.panel.setLayout(new FlowLayout());
		}
		else if (this.layout.compareTo("GRIDBAGLAYOUT") == 0) {
			this.panel.setLayout(new GridBagLayout());
			this.gridbagconst = new GridBagConstraints();

			this.gridbagconst.gridy = 1;
			this.gridbagconst.weightx = 0.5D;
			this.gridbagconst.weighty = 0.5D;
		}
	}

	private void updateLayout()
	{
		if (this.layout.compareTo("FLOWLAYOUT") != 0)
		{
			if (this.layout.compareTo("GRIDBAGLAYOUT") == 0) {
				this.gridbagconst.gridy += 1;
			}
		}

		//    pack();
	}

	protected void update()
	{
		for (int i = 0; i < this.groups.size(); ++i) {
			((FieldGroup)this.groups.get(i)).refresh();
		}
		setVisible(this.visible);
		if (!this.visible) {
			hide();
		}
		//    pack();
	}

	protected void performQuery() {
		//    enableFields(false);
		this.editPanel.setVisible(false);
		this.insertPanel.setVisible(false);
		this.queryPanel.setVisible(true);
		if (this.hasSubform) {
			this.subFormPanel.setVisible(true);
		} else {
			this.subFormPanel.setVisible(false);
		}
		this.panel.setVisible(true);

		//    this.mainPane.add(this.queryPanel, "South");
		//    pack();
	}

	public void startEdition()
	{
		saveFieldsInMemory();
		enableFields(true);
		this.insertPanel.setVisible(false);
		this.queryPanel.setVisible(false);
		this.editPanel.setVisible(true);
		this.subFormPanel.setVisible(false);
		this.panel.setVisible(true);
		add(this.editPanel, "South");
	}

	public void startDeletion()
	{
		int confirmacionBorrado = JOptionPane.showConfirmDialog(null, "¿Confirma que desea eliminar la entidad?");
		if (confirmacionBorrado == 0) {
			confirmDeletion();
		}
	}

	public void confirmEdition()
	{
		this.formController.notifyObservers();
		performQuery();
	}

	public void openSubForm(String nombre)
	{
		for (int i = 0; i < this.subforms.size(); ++i) {
			SubForm subformulario = (SubForm)this.subforms.get(i);
			if (!subformulario.getName().equals(nombre)) {
				continue;
			}
			String table = subformulario.getTable();
			String database = subformulario.getDataBase();
			String name = subformulario.getName();

			SubFormController subformController = new SubFormController(this, subformulario, name, database, table, "gridbaglayout", name, name);
			if (!this.subformsControllers.contains(subformController)) {
				this.subformsControllers.add(subformController);
			}

			SubFormInterface interfaz = subformController.getInterface();
			interfaz.addClaveForanea();
			interfaz.addPanelTipos(subformulario.getPrimaryField());
			interfaz.setResizable(false);
			interfaz.setVisible(true);
		}
	}

	public void cancelEdition()
	{
		loadMemory();
		performQuery();
	}

	protected void confirmDeletion()
	{
		this.formController.notifyDeletionToObservers();
	}

	protected void startInsertion() {
		enableFields(true);
		this.queryPanel.setVisible(false);
		this.editPanel.setVisible(false);
		this.insertPanel.setVisible(true);
		this.subFormPanel.setVisible(false);
		add(this.insertPanel, "South");
		//    pack();
	}

	protected void addSubForm(ArrayList subforms)
	{
		for (int i = 0; i < subforms.size(); ++i) {
			SubForm subform = (SubForm)subforms.get(i);
			this.subFormPanel.addButton(subform.getName());
		}

		JPanel miSubFormPanel = new JPanel(new BorderLayout());

		miSubFormPanel.setSize(400, 400);
		miSubFormPanel.add(this.subFormPanel, "East");

		this.hasSubform = true;
		this.panel.add(miSubFormPanel, this.gridbagconst);
	}

	public void confirmInsertion()
	{
		//    setModal(false);
		this.formController.notifyObservers();
	}

	public void cancelInsertion()
	{
		initFields();
		showInterface(false);
	}

	protected void loadData()
	{
		for (int i = 0; i < this.dependencies.size(); ++i) {
			((Dependency)this.dependencies.get(i)).loadData();
		}

		for (int i = 0; i < this.groups.size(); ++i) {
			((FieldGroup)this.groups.get(i)).loadData();
		}
	}

	private void enableFields(boolean enabled)
	{
		for (int i = 0; i < this.dependencies.size(); ++i) {
			((Dependency)this.dependencies.get(i)).enableFields(enabled);
		}

		for (int i = 0; i < this.groups.size(); ++i) {
			((FieldGroup)this.groups.get(i)).enableFields(enabled);
		}
	}

	protected void initFields()
	{
		for (int i = 0; i < this.dependencies.size(); ++i) {
			((Dependency)this.dependencies.get(i)).initFields();
		}

		for (int i = 0; i < this.groups.size(); ++i) {
			((FieldGroup)this.groups.get(i)).initFields();
		}
	}

	private void saveFieldsInMemory()
	{
		for (int i = 0; i < this.dependencies.size(); ++i) {
			((Dependency)this.dependencies.get(i)).saveInMemory();
		}

		for (int i = 0; i < this.groups.size(); ++i) {
			((FieldGroup)this.groups.get(i)).saveInMemory();
		}
	}

	private void loadMemory()
	{
		for (int i = 0; i < this.dependencies.size(); ++i) {
			((Dependency)this.dependencies.get(i)).loadMemory();
		}

		for (int i = 0; i < this.groups.size(); ++i) {
			((FieldGroup)this.groups.get(i)).loadMemory();
		}
	}

	//  protected void fillEntitiesPanel(Collection entityIds, Integer defaultEntity)
	//  {
	//    this.selectEntityPanel.putEntities(entityIds, defaultEntity);
	//  }

	//  protected void showEntitiesPanel(boolean visible) {
	//    this.selectEntityPanel.setVisible(visible);
	//  }

	public void fillValues() {
		for (int i=0; i<groups.size(); i++) {

			FieldGroup group = (FieldGroup) groups.get(i);
			ArrayList fields = group.getFieldsInterface();
			//		  ArrayList fields = group.getFields();
			for (int j=0; j<fields.size(); j++) {
				((FieldInterface) fields.get(j)).setValue("aa");
			}
		}
	}

	private void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void addPollButton() {
		if (formController.hasPollButton()) {
			if (!hasPollButton) {
				if (this.nucleosRelation!=null) {
					this.nucleosRelationPanel.add(pollButton);
				} else {
				panel.add(pollButton, this.gridbagconst);
				updateLayout();
				}
				pollButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent paramActionEvent) {
						formController.poll();
					}

				});
				hasPollButton = true;
			} else {
				pollButton.setVisible(true);
			}
		}
	}

	public void addNucleosRelationButton(NucleosRelation relation) {
		this.nucleosRelation = relation;
		this.nucleosRelationPanel = new NucleosRelationButtonPanel(this);
		this.panel.add(nucleosRelationPanel, this.gridbagconst);
		updateLayout();
	}

	public void removePollButton() {
		pollButton.setVisible(false);
	}

	public void enablePollButton(boolean enabled) {
		pollButton.setEnabled(enabled);
	}

	public NucleosRelation getNucleosRelation() {
		return nucleosRelation;
	}
}
