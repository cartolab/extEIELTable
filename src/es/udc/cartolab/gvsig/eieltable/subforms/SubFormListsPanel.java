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

package es.udc.cartolab.gvsig.eieltable.subforms;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;

import es.udc.cartolab.gvsig.eieltable.domain.Domain;
import es.udc.cartolab.gvsig.eieltable.domain.UserDomain;
import es.udc.cartolab.gvsig.eieltable.field.ComboFieldInterface;
import es.udc.cartolab.gvsig.eieltable.field.FieldController;
import es.udc.cartolab.gvsig.eieltable.field.FieldInterface;
import es.udc.cartolab.gvsig.eieltable.formgenerator.FormException;
import es.udc.cartolab.gvsig.eieltable.util.CampoVO;
import es.udc.cartolab.gvsig.eieltable.util.ForeignKeyVO;
import es.udc.cartolab.gvsig.eieltable.util.ObtenerDominioDAO;
import es.udc.cartolab.gvsig.eieltable.util.OtrosDatosVO;

public class SubFormListsPanel extends JPanel
{
	private ArrayList entityIds;
	private FieldInterface field;
	private UserDomain domain;
	private ForeignKeyVO claveForanea;
	private ObtenerDominioDAO obtenerDominioDAO;
	private String database;
	private String tabla;
	private SubFormController subformController;
	private JButton moveRightButton;
	private JButton moveLeftButton;
	private JButton moveAllRightButton;
	private JButton moveAllLeftButton;
	private JList assignedList;
	private JList availableList;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private MouseAdapter eventButton1;
	private MouseAdapter eventButton2;
	private MouseAdapter eventButton3;
	private MouseAdapter eventButton4;

	public SubFormListsPanel(SubFormController subformController, FieldInterface field, ForeignKeyVO claveForanea)
	{
		this.subformController = subformController;
		this.claveForanea = claveForanea;
		this.field = field;

		setBorder(BorderFactory.createTitledBorder("Tipos"));
		initComponents();
		ComboFieldInterface combo = (ComboFieldInterface)field;
		this.domain = combo.getDomain();

		this.database = subformController.getDataBase();
		this.tabla = subformController.getTable();
		try {
			this.obtenerDominioDAO = new ObtenerDominioDAO();
		} catch (FormException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reasignarDominiosIniciales();
	}

	private void initComponents()
	{
		this.jScrollPane1 = new JScrollPane();
		this.assignedList = new JList();
		this.jScrollPane2 = new JScrollPane();
		this.availableList = new JList();
		this.moveRightButton = new JButton();
		this.moveLeftButton = new JButton();
		this.moveAllRightButton = new JButton();
		this.moveAllLeftButton = new JButton();
		this.jLabel1 = new JLabel();
		this.jLabel2 = new JLabel();

		this.assignedList.setModel(new AbstractListModel() { String[] strings;

		public int getSize() { return this.strings.length; }
		public Object getElementAt(int i) { return this.strings[i];
		}
		});
		this.jScrollPane1.setViewportView(this.assignedList);

		this.availableList.setModel(new AbstractListModel() { String[] strings;

		public int getSize() { return this.strings.length; }
		public Object getElementAt(int i) { return this.strings[i];
		}
		});
		this.jScrollPane2.setViewportView(this.availableList);

		this.moveRightButton.setText(">");

		this.moveLeftButton.setText("<");

		this.moveAllRightButton.setText(">>");

		this.moveAllLeftButton.setText("<<");

		this.jLabel1.setText("Asignados");

		this.jLabel2.setText("Disponibles");

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(25, 25, 25).addComponent(this.jScrollPane1, -2, 128, -2).addGap(18, 18, 18).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.moveRightButton, -1, -1, 32767).addComponent(this.moveLeftButton, -1, -1, 32767).addComponent(this.moveAllRightButton, -1, -1, 32767).addComponent(this.moveAllLeftButton, -1, -1, 32767)).addGap(22, 22, 22).addComponent(this.jScrollPane2, -2, 121, -2).addContainerGap(32, 32767)).addGroup(layout.createSequentialGroup().addGap(61, 61, 61).addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 140, 32767).addComponent(this.jLabel2).addGap(116, 116, 116)));

		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(88, 88, 88).addComponent(this.moveRightButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.moveLeftButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.moveAllRightButton).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.moveAllLeftButton)).addGroup(layout.createSequentialGroup().addGap(49, 49, 49).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel1).addComponent(this.jLabel2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jScrollPane2, -2, -1, -2).addComponent(this.jScrollPane1, -2, -1, -2)))).addContainerGap(42, 32767)));
	}

	public void borrarDatos()
	{
		try {
			this.obtenerDominioDAO.borrarDatos(this.database, this.tabla, this.claveForanea);
		} catch (FormException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertarAsignados(Vector datos) {
		for (int i = 0; i < datos.size(); ++i) {
			try {
				this.obtenerDominioDAO.insertarDatos(this.domain, this.field.getField().getName(), (OtrosDatosVO)datos.get(i), this.database, this.tabla, this.claveForanea);
			} catch (FormException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Domain getDomain()
	{
		return this.domain;
	}

	public Vector obtenerDatosIniciales(SubForm subform, Vector datos, String nombreCampoPrimario) {
		Vector nombreCampos = new Vector();
		if (subform.getFields().size() != 0)
		{
			for (int i = 0; i < subform.getFields().size(); ++i) {
				FieldInterface field = (FieldInterface)subform.getFields().get(i);
				FieldController fieldControl = field.getField();
				CampoVO campo = new CampoVO();
				campo.setNombre(fieldControl.getName());
				nombreCampos.add(campo);
			}

		}

		//TODO repasar esto, no puede estar bien ni de lejos (los valores no los coge de BD!)
		for (int i = 0; i < this.assignedList.getModel().getSize(); ++i) {
			String valorCampoPrimario = (String)this.assignedList.getModel().getElementAt(i);
			OtrosDatosVO campos = new OtrosDatosVO();
			if (nombreCampos.size() != 0) {
				try {
					campos = this.obtenerDominioDAO.obtenerDatosIniciales(this.domain, nombreCampos, nombreCampoPrimario, valorCampoPrimario, this.database, this.tabla, this.claveForanea);
				} catch (FormException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				campos.setCodigo(valorCampoPrimario);
				campos.setDatos(new Vector());
			}
			datos.add(campos);
		}

		return datos;
	}

	public void reasignarDominiosIniciales()
	{
		Vector dominiosAsignados;
		try {
			dominiosAsignados = this.obtenerDominioDAO.obtenerDominiosAsignados(this.field.getField().getName(), this.field.getField().getDomain().getName(), this.database, this.tabla, this.claveForanea);
			Vector nomDominiosAsignados = new Vector();
			for (int i = 0; i < dominiosAsignados.size(); ++i) {
				nomDominiosAsignados.add(this.domain.resolve((String)dominiosAsignados.get(i)));
			}

			this.assignedList.setListData(nomDominiosAsignados);
		} catch (FormException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Vector domains = new Vector();
		for (int i = 0; i < this.domain.getValues().size(); ++i) {
			domains.add(this.domain.getValues().get(i));
		}

		for (int i = 0; i < this.assignedList.getModel().getSize(); ++i) {
			String item = (String)this.assignedList.getModel().getElementAt(i);
			if (!domains.contains(item)) {
				continue;
			} domains.remove(item);
		}
		this.availableList.setListData(domains);
	}

	private void jButtonDesasignarMouseClicked(MouseEvent evt)
	{
		Vector valoresList2 = new Vector();
		Vector valoresSeleccionados = new Vector();
		Vector valoresFinalesList2 = new Vector();
		Vector vacio = new Vector();

		int[] indicesValoresSeleccionados = this.assignedList.getSelectedIndices();
		int indexValSele = 0;
		for (int i = 0; i < indicesValoresSeleccionados.length; ++i) {
			String nombre = (String)this.assignedList.getModel().getElementAt(indicesValoresSeleccionados[i]);
			valoresSeleccionados.add(nombre);
			this.subformController.getInterface().eliminarDatos(nombre);

			++indexValSele;
		}

		Vector list1Vector = new Vector();

		for (int i = 0; i < this.assignedList.getModel().getSize(); ++i) {
			list1Vector.add(this.assignedList.getModel().getElementAt(i));
		}

		for (int j = 0; j < valoresSeleccionados.size(); ++j) {
			if (list1Vector.contains(valoresSeleccionados.get(j))) {
				int posicion = list1Vector.indexOf(valoresSeleccionados.get(j));
				list1Vector.remove(posicion);
			}

		}

		this.assignedList.setListData(list1Vector);

		for (int i = 0; i < this.availableList.getModel().getSize(); ++i) {
			valoresList2.add(this.availableList.getModel().getElementAt(i));
		}

		for (int i = 0; i < valoresSeleccionados.size(); ++i) {
			valoresList2.add(valoresSeleccionados.get(i));
		}
		this.availableList.setListData(valoresList2);
	}

	private void jButtonAsignarMouseClicked(MouseEvent evt)
	{
		Vector valoresList1 = new Vector();
		Vector valoresSeleccionados = new Vector();
		Vector valoresFinalesList2 = new Vector();
		Vector vacio = new Vector();

		int[] indicesValoresSeleccionados = SubFormListsPanel.this.availableList.getSelectedIndices();

		for (int i = 0; i < indicesValoresSeleccionados.length; ++i) {

			String nombre = (String)SubFormListsPanel.this.availableList.getModel().getElementAt(indicesValoresSeleccionados[i]);
			SubFormListsPanel.this.subformController.getInterface().setItem(nombre);

			SubFormListsPanel.this.subformController.getInterface().insertarDatosParaDisponibles(nombre);
			if (SubFormListsPanel.this.subformController.getInterface().getSubForm().getFields().size() != 0) {
				SubFormListsPanel.this.subformController.getInterface().setProcesado(false);
			}

			if (SubFormListsPanel.this.subformController.getInterface().getAsignar()) {
				valoresSeleccionados.add(SubFormListsPanel.this.subformController.getInterface().getItem());
			}

		}

		Vector list2Vector = new Vector();

		for (int i = 0; i < SubFormListsPanel.this.availableList.getModel().getSize(); ++i) {
			list2Vector.add(SubFormListsPanel.this.availableList.getModel().getElementAt(i));
		}

		for (int j = 0; j < valoresSeleccionados.size(); ++j) {
			if (list2Vector.contains(valoresSeleccionados.get(j))) {
				int posicion = list2Vector.indexOf(valoresSeleccionados.get(j));
				list2Vector.remove(posicion);
			}
		}

		SubFormListsPanel.this.availableList.setListData(list2Vector);

		for (int i = 0; i < SubFormListsPanel.this.assignedList.getModel().getSize(); ++i) {
			valoresList1.add(SubFormListsPanel.this.assignedList.getModel().getElementAt(i));
		}

		for (int j = 0; j < valoresSeleccionados.size(); ++j) {
			valoresList1.add(valoresSeleccionados.get(j));
		}

		SubFormListsPanel.this.assignedList.setListData(valoresList1);


	}

	private void jButtonDesasignarTodosMouseClicked(MouseEvent evt)
	{
		Vector valoresList1 = new Vector();
		Vector valoresList2 = new Vector();
		Vector vacio = new Vector();

		for (int i = 0; i < this.assignedList.getModel().getSize(); ++i) {
			String nombre = (String)this.assignedList.getModel().getElementAt(i);
			valoresList1.add(nombre);
			this.subformController.getInterface().eliminarDatos(nombre);
		}
		this.assignedList.setListData(vacio);

		for (int i = 0; i < this.availableList.getModel().getSize(); ++i) {
			valoresList2.add(this.availableList.getModel().getElementAt(i));
		}

		for (int j = 0; j < valoresList1.size(); ++j) {
			valoresList2.add(valoresList1.get(j));
		}

		this.availableList.setListData(valoresList2);
	}

	private void jButtonAsignarTodosMouseClicked(MouseEvent evt)
	{

		Vector valoresSeleccionados = new Vector();
		Vector valoresList1 = new Vector();
		Vector valoresList2 = new Vector();

		for (int i = 0; i < SubFormListsPanel.this.assignedList.getModel().getSize(); ++i) {
			valoresList1.add(SubFormListsPanel.this.assignedList.getModel().getElementAt(i));
		}

		for (int i = 0; i < SubFormListsPanel.this.availableList.getModel().getSize(); ++i) {
			valoresList2.add(SubFormListsPanel.this.availableList.getModel().getElementAt(i));
		}

		for (int i = 0; i < SubFormListsPanel.this.availableList.getModel().getSize(); ++i) {
			String nombre = (String)SubFormListsPanel.this.availableList.getModel().getElementAt(i);
			SubFormListsPanel.this.subformController.getInterface().setItem(nombre);

			SubFormListsPanel.this.subformController.getInterface().insertarDatosParaDisponibles(nombre);
			SubFormListsPanel.this.subformController.getInterface().setProcesado(false);

			if (SubFormListsPanel.this.subformController.getInterface().getAsignar()) {
				valoresSeleccionados.add(SubFormListsPanel.this.subformController.getInterface().getItem());
			}
		}
		for (int j = 0; j < valoresSeleccionados.size(); ++j) {
			if (valoresList2.contains(valoresSeleccionados.get(j))) {
				int posicion = valoresList2.indexOf(valoresSeleccionados.get(j));
				valoresList2.remove(posicion);
			}
		}
		SubFormListsPanel.this.availableList.setListData(valoresList2);

		for (int i = 0; i < valoresSeleccionados.size(); ++i) {
			valoresList1.add(valoresSeleccionados.get(i));
		}
		SubFormListsPanel.this.assignedList.setListData(valoresList1);

	}

	public JList getJListAsignados()
	{
		return this.assignedList;
	}

	public void deshabilitar()
	{
		this.moveRightButton.setEnabled(false);
		this.moveLeftButton.setEnabled(false);
		this.moveAllRightButton.setEnabled(false);
		this.moveAllLeftButton.setEnabled(false);
		this.moveRightButton.removeMouseListener(this.eventButton1);
		this.moveLeftButton.removeMouseListener(this.eventButton2);
		this.moveAllRightButton.removeMouseListener(this.eventButton3);
		this.moveAllLeftButton.removeMouseListener(this.eventButton4);
		setEnabled(false);
	}

	public void habilitarBotones()
	{
		this.eventButton1 = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				SubFormListsPanel.this.jButtonDesasignarMouseClicked(evt);
			}
		};
		this.moveRightButton.addMouseListener(this.eventButton1);

		this.eventButton2 = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				SubFormListsPanel.this.jButtonAsignarMouseClicked(evt);
			}
		};
		this.moveLeftButton.addMouseListener(this.eventButton2);

		this.eventButton3 = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				SubFormListsPanel.this.jButtonDesasignarTodosMouseClicked(evt);
			}
		};
		this.moveAllRightButton.addMouseListener(this.eventButton3);

		this.eventButton4 = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				SubFormListsPanel.this.jButtonAsignarTodosMouseClicked(evt);
			}
		};
		this.moveAllLeftButton.addMouseListener(this.eventButton4);

		this.moveRightButton.setEnabled(true);
		this.moveLeftButton.setEnabled(true);
		this.moveAllRightButton.setEnabled(true);
		this.moveAllLeftButton.setEnabled(true);
	}
}
