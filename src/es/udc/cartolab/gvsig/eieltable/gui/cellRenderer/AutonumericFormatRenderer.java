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

package es.udc.cartolab.gvsig.eieltable.gui.cellRenderer;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;



/**
 * Decimal Format Renderer.
 * 
 * The renderer used for formatting each decimal in the proper way,
 * with the requirements provided by their restrictions.
 */
public class AutonumericFormatRenderer extends DefaultTableCellRenderer {

	/**
	 * Decimal Format Renderer Constructor.
	 * 
	 * The public constructor, which accepts an ArrayList of Restrictions
	 * in order to set the formatter with the proper number of decimals.
	 */
	public AutonumericFormatRenderer() {
		super();
		this.setHorizontalAlignment(JLabel.RIGHT);

		this.setBackground(Color.LIGHT_GRAY);
	}

	@Override
	public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		// First format the cell value as required

		if ((value != null) && !(("").equals(value))) {
			value = new Integer(value.toString());
		}

		// And pass it on to parent class

		return super.getTableCellRendererComponent(
				table, value, isSelected, hasFocus, row, column );
	}
}
