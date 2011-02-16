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

package es.udc.cartolab.gvsig.eielforms.gui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import es.udc.cartolab.gvsig.eielforms.field.FieldController;
import es.udc.cartolab.gvsig.eielforms.formgenerator.FormException;

public class EditAlphanumericForm extends AlphanumericForm {

	private int pos = -1;
	private JButton saveButton, cancelButton;
	private JPanel southPanel;
	private AlphanumericForm source = null;

	public EditAlphanumericForm(String formName) {
		super(formName + "_edit");
	}

	public EditAlphanumericForm(String formName, int pos) {
		this(formName);
		this.pos = pos;
	}

	public EditAlphanumericForm(AlphanumericForm source, String formName, int pos) {
		this(formName, pos);
		this.source = source;
	}

	public EditAlphanumericForm(AlphanumericForm source, String formName) {
		this(formName);
		this.source = source;
	}

	protected JPanel getButtonsPanel() {
		if (southPanel == null) {
			southPanel = new JPanel();
			saveButton = new JButton("Guardar");
			saveButton.addActionListener(this);
			southPanel.add(saveButton);
			cancelButton = new JButton("Cancelar");
			cancelButton.addActionListener(this);
			southPanel.add(cancelButton);
		}
		return southPanel;
	}

	private boolean save() {
		if (form.validate()) {
			if (pos<0) {
				form.insert();
			} else {
				form.update(key);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() == saveButton) {
			if (!save()) {
				System.out.println("Error guardando datos");
			} else {
				close();
			}
		}
		if (arg0.getSource() == cancelButton) {
			close();
		}
	}

	/**
	 * To get key of the table based on pos attribute, only used in open.
	 */
	protected boolean setKey() throws FormException {
		ArrayList keyFields = form.getKey();
		key = new HashMap();
		if (keyFields.size()!=1) {
			throw new FormException("Clave primaria mal formada en el formulario");
		}
		FieldController fc = (FieldController) keyFields.get(0);
		String position = Integer.toString(pos);
		key.put(fc.getName(), position);
		return true;
	}

	public void fillValues() {
		super.fillValues();
	}

	protected void addPKChangeListener() {
		//here we don't want to listen pk changes, it won't change.
	}

	public void close() {
		super.close();
		if (source!=null) {
			boolean fill = true;
			if (pos==-1) {
				try {
					fill = source.setKey();
				} catch (FormException e) {
					fill = false;
				}
			}
			if (fill) {
				source.fillValues();
			}
		}
	}

}
