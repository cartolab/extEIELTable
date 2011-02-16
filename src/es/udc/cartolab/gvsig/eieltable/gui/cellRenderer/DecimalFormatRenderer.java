package es.udc.cartolab.gvsig.eieltable.gui.cellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import es.udc.cartolab.gvsig.eieltable.domain.restriction.DecimalSizeRestriction2;
import es.udc.cartolab.gvsig.eieltable.domain.restriction.Restriction2;



/**
 * Decimal Format Renderer.
 * 
 * The renderer used for formatting each decimal in the proper way,
 * with the requirements provided by their restrictions.
 */
public class DecimalFormatRenderer extends DefaultTableCellRenderer {
	/**
	 * The format that will be applied to the numbers.
	 */
	private static DecimalFormat formatter;

	/**
	 * Decimal Format Renderer Constructor.
	 * 
	 * The public constructor, which accepts an ArrayList of Restrictions
	 * in order to set the formatter with the proper number of decimals.
	 */
	public DecimalFormatRenderer(ArrayList<Restriction2> restrictions, boolean editable) {
		super();
		this.setHorizontalAlignment(JLabel.RIGHT);

		if (!editable) {
			this.setBackground(Color.GRAY);
		}

		int i, j;
		String format = "0.";
		int decimals = 0;
		for (i=0; i<restrictions.size(); i++) {
			if (restrictions.get(i) instanceof DecimalSizeRestriction2) {
				if (((DecimalSizeRestriction2) restrictions.get(i)).getDecimalSize() > decimals) {
					decimals = ((DecimalSizeRestriction2) restrictions.get(i)).getDecimalSize();
				}
			}
		}
		for (j=0; j<decimals; j++) {
			format += "0";
		}
		formatter = new DecimalFormat( format, new DecimalFormatSymbols(Locale.US));
	}

	@Override
	public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		// First format the cell value as required

		value = formatter.format(value);

		// And pass it on to parent class

		return super.getTableCellRendererComponent(
				table, value, isSelected, hasFocus, row, column );
	}
}
