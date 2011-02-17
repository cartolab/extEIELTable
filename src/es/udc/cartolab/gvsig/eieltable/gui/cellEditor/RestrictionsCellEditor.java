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

package es.udc.cartolab.gvsig.eieltable.gui.cellEditor;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import es.udc.cartolab.gvsig.eieltable.domain.restriction.Restriction2;



/**
 * Restrictions Based Cell Editor.
 * 
 * The cell editor used for taking into consideration the
 * restrictions of each field before saving the value.
 */
public class RestrictionsCellEditor extends DefaultCellEditor
{

	/**
	 * Restrictions this editor must take into account before saving.
	 */
	private ArrayList<Restriction2> restrictions;

	/**
	 * Restrictions Based Cell Editor Constructor.
	 * 
	 * This constructor takes the restrictions as a parameter
	 * and stores them for checking values later.
	 * 
	 * @param restrictions the restrictions we want to be checked before saving.
	 * 
	 * @return A new RestrictionsCellEditor.
	 */
	public RestrictionsCellEditor(ArrayList<Restriction2> restrictions)
	{
		super(new JTextField()); //make the superclass based on a JTextField
		this.restrictions = restrictions;

		// We create a tooltip with the restrictions so they can be seen by the user.
		String tooltip = "";
		int i;
		for (i = 0; i<restrictions.size(); i++) {
			tooltip += "- " + restrictions.get(i).toString() + " -";
		}
		((JTextField) getComponent()).setToolTipText(tooltip);

		// Now we link a method with the Escape key stroke (for disabling
		// the red border when editing is cancelled that way).
		((JTextField) getComponent()).getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
		"cancel_edition");
		((JTextField) getComponent()).getActionMap().put("cancel_edition",
				new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				cancelCellEditing();
			}
		}
		);

	}

	@Override
	/**
	 * Stop Cell Editing.
	 * 
	 * This function is called when editing stops. We check everything
	 * is OK. If it is, we return the base class method. If it is
	 * not, we return false, and editing continues
	 * 
	 * @return A boolean, which is true if value is valid, and false if it isn't.
	 */
	public boolean stopCellEditing() {
		String value = ((JTextField)getComponent()).getText();
		Integer i;
		boolean valid = true;

		// is it valid according to the restrictions?
		for (i=0; i<restrictions.size(); i++) {
			valid = valid && restrictions.get(i).validate(value);
		}
		if (!valid) {
			((JTextField) getComponent()).setBorder(BorderFactory.createLineBorder(Color.RED));
			return false;
		}
		((JTextField) getComponent()).setBorder(null);
		return super.stopCellEditing();
	}

	// This is called when editing is cancelled.
	@Override
	/**
	 * Cancel Cell Editing.
	 * 
	 * This is called when editing is explicitly cancelled.
	 * 
	 */
	public void cancelCellEditing() {
		((JTextField) getComponent()).setBorder(null);
		super.cancelCellEditing();
	}
}
