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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import es.udc.cartolab.gvsig.eieltable.gui.cellEditor.RestrictionsCellEditor;
import es.udc.cartolab.gvsig.eieltable.gui.cellRenderer.AutonumericFormatRenderer;
import es.udc.cartolab.gvsig.eieltable.gui.cellRenderer.DecimalFormatRenderer;
import es.udc.cartolab.gvsig.eieltable.structure.SingleField;
import es.udc.cartolab.gvsig.eieltable.structure.domain.BasicDomain;
import es.udc.cartolab.gvsig.eieltable.structure.domain.UserDomain;



/**
 * Table Frame.
 * 
 * Customized frame for displaying editable tables.
 * 
 */
public class TableFrame extends JPanel {
	private boolean DEBUG = false;
	private JTable table;
	private ArrayList<SingleField> fields;
	private HashSet<Integer> modified = new HashSet<Integer>();
	private HashMap<Integer,HashMap<String,Object>> deleted = new HashMap<Integer,HashMap<String,Object>>();

	public TableFrame(ArrayList<SingleField> fields, Object[][] data) {
		super(new GridLayout(1,0));

		this.fields = fields;
		int i, j;

		for (i=0; i<fields.size(); i++) {
			if (fields.get(i).getDomain().getName().equals("disyuntiva")) {
				for (j=0; j<data.length; j++) {
					if (data[j][i] != null) {
						data[j][i] = ((String)data[j][i]).equals("SI");
					} else data[j][i] = false;
				}
			} else {
				for (j=0; j<data.length; j++) {
					if (data[j][i] == null) {
						data[j][i] = "";
					}
				}
			}
		}

		table = new JTable(new MyTableModel(fields, data));
		table.setPreferredScrollableViewportSize(new Dimension(fields.size()*150, 70));
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		//Set up column sizes.
		initColumnSizes(table);

		for (i=0; i<fields.size(); i++) {
			if ((fields.get(i).getDomain().getType().equals("usuario")) && !(fields.get(i).getDomain().toString().startsWith("disyuntiva"))) {
				setComboBoxColumn(table, table.getColumnModel().getColumn(i), ((UserDomain) fields.get(i).getDomain()).getValues());
			}
			if (fields.get(i).getDomain().getType().equals("basico")) {
				table.getColumnModel().getColumn(i).setCellEditor(new RestrictionsCellEditor(((BasicDomain) fields.get(i).getDomain()).getRestrictions()));
			}
			if ((fields.get(i).getDomain().getType().equals("basico")) && (fields.get(i).getDomain().getName().startsWith("numerico")) && !(fields.get(i).getDomain().toString().startsWith("int"))) {
				table.getColumnModel().getColumn(i).setCellRenderer(new DecimalFormatRenderer(((BasicDomain) fields.get(i).getDomain()).getRestrictions(), true));
			}
			if (fields.get(i).getDomain().getName().equals("Autonumerico")) {
				table.getColumnModel().getColumn(i).setCellRenderer(new AutonumericFormatRenderer());
			}

		}

		//Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		//Add the scroll pane to this panel.
		add(scrollPane);
	}

	/*
	 * This method picks good column sizes.
	 * If all column heads are wider than the column's cells'
	 * contents, then you can just use column.sizeWidthToFit().
	 */
	private void initColumnSizes(JTable table) {

		TableColumn column = null;
		Component comp = null;
		int maxWidth, length;
		TableCellRenderer headerRenderer =
			table.getTableHeader().getDefaultRenderer();

		for (int i = 0; i < table.getColumnCount(); i++) {
			column = table.getColumnModel().getColumn(i);

			comp = headerRenderer.getTableCellRendererComponent(
					null, column.getHeaderValue(),
					false, false, 0, 0);
			maxWidth = comp.getPreferredSize().width;

			List<Object[]> data = ((MyTableModel) this.table.getModel()).getData();

			for (int j = 0; j < data.size(); j++) {
				if (data.get(j)[i] != null) {
					length = data.get(j)[i].toString().length();
					if (maxWidth < length) {
						maxWidth = length;
					}
				}
			}

			if (DEBUG) {
				System.out.println("Initializing width of column "
						+ i + ". "
						+ "maxWidth = " + (maxWidth + 50));
			}

			column.setPreferredWidth(maxWidth+50);
		}
	}

	public void setComboBoxColumn(JTable table, TableColumn column, ArrayList<String> values) {
		//Set up the editor for the combobox cells.
		JComboBox comboBox = new JComboBox();
		int i;
		for(i=0; i<values.size(); i++) {
			comboBox.addItem(values.get(i));
		}
		column.setCellEditor(new DefaultCellEditor(comboBox));

		//Set up tool tips for the sport cells.
		DefaultTableCellRenderer renderer =
			new DefaultTableCellRenderer();
		renderer.setToolTipText("Click for combo box");
		column.setCellRenderer(renderer);
	}

	public void setData(Object [][] data) {
		((MyTableModel) this.table.getModel()).setData(data);
		if (data.length > 0) {
			((MyTableModel) this.table.getModel()).setLongValues(data[0]);
		}
		int i, j;

		for (i=0; i<fields.size(); i++) {
			if (fields.get(i).getDomain().getName().equals("disyuntiva")) {
				for (j=0; j<data.length; j++) {
					if (data[j][i] != null) {
						data[j][i] = ((String)data[j][i]).equals("SI");
					}
				}
			}
		}
	}

	public ArrayList<ArrayList<HashMap<String,Object>>> getModifiedRows() {
		HashSet<Integer> aux = modified;
		Iterator<Integer> iter = aux.iterator();

		ArrayList<ArrayList<HashMap<String,Object>>> results = new ArrayList<ArrayList<HashMap<String,Object>>>();

		while (iter.hasNext()) {
			HashMap<String,Object> keys = new HashMap<String,Object>();
			HashMap<String,Object> values = new HashMap<String,Object>();
			ArrayList<HashMap<String,Object>> row = new ArrayList<HashMap<String,Object>>();
			int i, n = iter.next();

			for (i=0; i<fields.size(); i++) {
				if (fields.get(i).getDomain().getName().equals("Autonumerico")) {
					if ((((MyTableModel) table.getModel()).getInnerValueAt(n, i) == null) ||
							("".equals(((MyTableModel) table.getModel()).getInnerValueAt(n, i)))) {
						continue;
					} else {
						keys.put(fields.get(i).getName(), ((MyTableModel) table.getModel()).getInnerValueAt(n, i));
					}
				} else {
					values.put(fields.get(i).getName(), ((MyTableModel) table.getModel()).getInnerValueAt(n, i));
				}
			}

			row.add(keys);
			row.add(values);
			results.add(row);
		}

		return results;
	}

	public HashMap<Integer,HashMap<String,Object>> getNewRows() {
		HashSet<Integer> aux = modified;

		Iterator<Integer> iter = aux.iterator();

		HashMap<Integer,HashMap<String,Object>> results = new HashMap<Integer,HashMap<String,Object>>();

		while (iter.hasNext()) {
			HashMap<String,Object> values = new HashMap<String,Object>();
			int i, n = iter.next();
			boolean newRow = true;

			for (i=0; i<fields.size(); i++) {
				if (fields.get(i).getDomain().getName().equals("Autonumerico")) {
					if (!((((MyTableModel) table.getModel()).getInnerValueAt(n, i) == null) ||
							("".equals(((MyTableModel) table.getModel()).getInnerValueAt(n, i))))) {
						newRow = false;
					}
				} else {
					values.put(fields.get(i).getName(), ((MyTableModel) table.getModel()).getInnerValueAt(n, i));
				}
			}

			if (newRow) {
				results.put(n, values);
			}
		}

		return results;
	}

	public ArrayList<HashMap<String,Object>> getDeletedRows() {
		HashMap<Integer,HashMap<String,Object>> aux = deleted;
		Iterator<HashMap<String,Object>> iter = aux.values().iterator();
		ArrayList<HashMap<String,Object>> results = new ArrayList<HashMap<String,Object>>();

		while (iter.hasNext()) {
			results.add(iter.next());
		}

		return results;
	}

	public void createRow() {
		TableRowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>((MyTableModel)table.getModel());
		ArrayList<SortKey> sortKeys = new ArrayList<SortKey>();
		sortKeys.add(new SortKey(0, SortOrder.DESCENDING));
		sorter.setSortKeys(sortKeys);
		table.setRowSorter(sorter);

		int row = ((MyTableModel) this.table.getModel()).createRow();
		table.scrollRectToVisible(table.getCellRect(row, 0, true));
		table.getSelectionModel().setSelectionInterval(row, row);

	}

	/* Please bear in mind that this method voids the table
	 * without noting down the deleted rows. It's intended
	 * for deleting rows which make no sense on DB.  */
	public void voidTableDirectly() {
		this.table.selectAll();

		int [] rows = this.table.getSelectedRows();
		ArrayList<Integer> rowsC = new ArrayList<Integer>();
		Arrays.sort(rows);
		int rowC;

		for (int i = 0; i < rows.length; i++) {
			rowsC.add(table.convertRowIndexToModel(rows[i]));
		}

		Collections.sort(rowsC);

		for (int i = rowsC.size()-1; i>=0; i--) {
			rowC = rowsC.get(i);

			((MyTableModel) this.table.getModel()).deleteRow(rowC, table.convertRowIndexToView(rowC));
		}
	}

	public void deleteSelectedRows() {

		int [] rows = this.table.getSelectedRows();
		ArrayList<Integer> rowsC = new ArrayList<Integer>();
		Arrays.sort(rows);
		int rowC;

		for (int i = 0; i < rows.length; i++) {
			rowsC.add(table.convertRowIndexToModel(rows[i]));
		}

		Collections.sort(rowsC);

		for (int i = rowsC.size()-1; i>=0; i--) {
			rowC = rowsC.get(i);
			HashMap<String,Object> keys = new HashMap<String,Object>();
			HashMap<Integer,Object> keysAux = new HashMap<Integer,Object>();

			boolean exists = true;
			for (int j=0; j<fields.size(); j++) {
				if (fields.get(j).getDomain().getName().equals("Autonumerico")) {
					if (this.table.getModel().getValueAt(rowC, j) == null) {
						exists = false;
						break;
					} else if (this.table.getModel().getValueAt(rowC, j).equals("")) {
						exists = false;
						break;
					}
					keys.put(fields.get(j).getName(), this.table.getModel().getValueAt(rowC, j));
					keysAux.put(j, this.table.getModel().getValueAt(rowC, j));
				}
			}

			if (exists) {
				this.deleted.put(rowC, keys);
			}
			if (this.modified.contains(rowC)) {
				this.modified.remove(rowC);
			} else {
				Iterator<Integer> iter = this.modified.iterator();
				HashSet<Integer> aux = new HashSet<Integer>();

				while (iter.hasNext()) {
					Integer value = iter.next();
					if (value > rowC) {
						aux.add(value-1);
					} else {
						aux.add(value);
					}
				}
				this.modified.clear();
				this.modified.addAll(aux);
			}

			((MyTableModel) this.table.getModel()).deleteRow(rowC, table.convertRowIndexToView(rowC));
		}
	}

	public void setSavedValues(HashMap<Integer,HashMap<String,Object>> values) {
		Set<Integer> rows = values.keySet();
		Iterator<Integer> iter = rows.iterator();
		while (iter.hasNext()) {
			int key = iter.next();
			HashMap<String,Object> rowValues = values.get(key);
			Iterator<String> iter2 = rowValues.keySet().iterator();

			while(iter2.hasNext()) {
				String field = iter2.next();
				for(int i = 0; i < this.fields.size(); i++) {
					if (this.fields.get(i).getName().equals(field)) {
						((MyTableModel) this.table.getModel()).setInnerValueAt(rowValues.get(field), key, i);
					}
				}
			}
		}
	}

	public void clearPendingChanges() {
		modified.clear();
		deleted.clear();
	}

	class MyTableModel extends AbstractTableModel {
		private ArrayList<SingleField> fields;
		private List<Object[]> data;
		private Object[] longValues;

		public MyTableModel(ArrayList<SingleField> fields) {
			this.fields = fields;
		}

		public MyTableModel(ArrayList<SingleField> fields, Object[][] data) {
			this.fields = fields;
			this.data = new ArrayList<Object[]>(Arrays.asList(data));
			if (data.length > 0) {
				this.longValues = data[0].clone();
				int i;
				for (i=0; i<longValues.length; i++) {
					longValues[i] = null;
				}
			}
		}

		public void setData(Object [][] data) {
			this.data = Arrays.asList(data);
			if (data.length > 0) {
				this.longValues = data[0];
			}
		}

		public List<Object[]> getData() {
			return data;
		}

		public int createRow() {
			data.add(longValues.clone());
			fireTableRowsInserted(data.size()-1,data.size()-1);
			return data.size()-1;
		}

		public void deleteRow(int modelRow, int row) {
			this.data.remove(modelRow);
			fireTableRowsDeleted(modelRow, modelRow);
		}

		public void setLongValues(Object [] longValues) {
			this.longValues = longValues;
		}

		@Override
		public int getColumnCount() {
			return fields.size();
		}

		@Override
		public int getRowCount() {
			if (data == null) {
				return 0;
			}
			return data.size();
		}

		@Override
		public String getColumnName(int col) {
			return fields.get(col).getLabel();
		}

		public Object getInnerValueAt(int row, int col) {
			if ((fields.get(col).getDomain().getType().equals("basico")) && (fields.get(col).getDomain().getName().startsWith("numerico")) && !(fields.get(col).getDomain().toString().startsWith("int"))) {
				return Double.valueOf((String)data.get(row)[col]);
			}
			return data.get(row)[col];
		}

		@Override
		public Object getValueAt(int row, int col) {
			if ((fields.get(col).getDomain().getType().equals("basico")) && (fields.get(col).getDomain().getName().startsWith("numerico")) && !(fields.get(col).getDomain().toString().startsWith("int"))) {
				return Double.valueOf((String)data.get(row)[col]);
			}
			if ((fields.get(col).getDomain().getType().equals("usuario")) && !(fields.get(col).getDomain().toString().startsWith("disyuntiva"))) {
				return ((UserDomain) fields.get(col).getDomain()).resolve((String) data.get(row)[col]);
			}
			return data.get(row)[col];
		}

		/*
		 * JTable uses this method to determine the default renderer/
		 * editor for each cell.  If we didn't implement this method,
		 * then the last column would contain text ("true"/"false"),
		 * rather than a check box.
		 */
		@Override
		public Class getColumnClass(int c) {
			if ((fields.get(c).getDomain().getType().equals("basico")) && ((fields.get(c).getDomain().toString().startsWith("int")) || (fields.get(c).getDomain().toString().startsWith("numerico")))) {
				return Integer.class;
			}
			if ((fields.get(c).getDomain().getType().equals("basico")) && (fields.get(c).getDomain().getName().startsWith("numerico"))) {
				return Double.class;
			}
			if ((fields.get(c).getDomain().getType().equals("usuario")) && (fields.get(c).getDomain().toString().startsWith("disyuntiva"))) {
				return Boolean.class;
			}
			return String.class;
		}

		/*
		 * Don't need to implement this method unless your table's
		 * editable.
		 */
		@Override
		public boolean isCellEditable(int row, int col) {
			//Note that the data/cell address is constant,
			//no matter where the cell appears onscreen.
			return !(fields.get(col).getDomain().getName().equals("Autonumerico"));
		}

		/*
		 * Don't need to implement this method unless your table's
		 * data can change.
		 */
		@Override
		public void setValueAt(Object value, int row, int col) {

			if (DEBUG) {
				System.out.println("Setting value at " + row + "," + col
						+ " to " + value
						+ " (an instance of "
						+ value.getClass() + ")");
			}

			if ((fields.get(col).getDomain().getType().equals("basico")) && (fields.get(col).getDomain().getName().startsWith("numerico")) && !(fields.get(col).getDomain().toString().startsWith("int"))) {
				data.get(row)[col] = value.toString();
			} else if ((fields.get(col).getDomain().getType().equals("usuario")) && !(fields.get(col).getDomain().toString().startsWith("disyuntiva"))) {
				data.get(row)[col] = ((UserDomain) fields.get(col).getDomain()).unResolve((String) value);
			} else {
				data.get(row)[col] = value;
			}

			fireTableCellUpdated(row, col);

			modified.add(row);

			if (DEBUG) {
				System.out.println("Modified: " + row);
				System.out.println("New value of data:");
				printDebugData();
			}
		}

		public void setInnerValueAt(Object value, int row, int col) {

			if (DEBUG) {
				System.out.println("Setting inner value at " + row + "," + col
						+ " to " + value
						+ " (an instance of "
						+ value.getClass() + ")");
			}

			if ((fields.get(col).getDomain().getType().equals("basico")) && (fields.get(col).getDomain().getName().startsWith("numerico")) && !(fields.get(col).getDomain().toString().startsWith("int"))) {
				data.get(row)[col] = value.toString();
			} else {
				data.get(row)[col] = value;
			}

			fireTableCellUpdated(row, col);
		}

		private void printDebugData() {
			int numRows = getRowCount();
			int numCols = getColumnCount();

			for (int i=0; i < numRows; i++) {
				System.out.print("    row " + i + ":");
				for (int j=0; j < numCols; j++) {
					System.out.print("  " + data.get(i)[j]);
				}
				System.out.println();
			}
			System.out.println("--------------------------");
		}


	}

}
