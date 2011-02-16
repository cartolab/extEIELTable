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
