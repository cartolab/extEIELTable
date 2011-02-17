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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.border.TitledBorder;

import es.udc.cartolab.gvsig.eieltable.domain.UserDomain;
import es.udc.cartolab.gvsig.eieltable.field.FieldController;
import es.udc.cartolab.gvsig.eieltable.field.FieldInterface;
import es.udc.cartolab.gvsig.eieltable.forms.FormInterface;
import es.udc.cartolab.gvsig.eieltable.util.CampoVO;
import es.udc.cartolab.gvsig.eieltable.util.ForeignKeyVO;
import es.udc.cartolab.gvsig.eieltable.util.OtrosDatosVO;

public class SubFormInterface extends JDialog
{
	private String layout;
	private SubFormController subformController;
	private ArrayList groups;
	private ArrayList dependencies;
	private SubForm subform;
	private JRootPane mainPane;
	private JPanel panel;
	private JPanel titlePanel;
	private GridBagConstraints gridbagconst;
	private GridBagConstraints gridbagconst2;
	private boolean visible;
	private SubFormButtonsPanel panelBotones;

	private SubFormListsPanel panelTipos;
	private FormInterface formInterface;
	private String fase;
	private ForeignKeyVO claveForanea;
	private FieldInterface primaryField;

	private SubFormOtherFieldsPanel subformPanel;
	private JDialog dialog;
	private SubFormOtherFieldsButtonsPanel panelBotonesModificarRestoCampos;
	private String item;
	private Vector datos;
	private boolean asignar = false;
	private boolean seleccionVarios = false;
	private boolean mostrarSiguiente = false;
	private boolean procesado = false;

	public SubFormInterface(FormInterface formInterface, SubForm subform, SubFormController subController, String layout, String title)
	{
		this.subformController = subController;
		this.gridbagconst = new GridBagConstraints();
		this.formInterface = formInterface;
		this.datos = new Vector();
		this.groups = new ArrayList();
		this.subform = subform;
		this.dependencies = new ArrayList();
		this.layout = layout.toUpperCase();
		this.visible = false;
		setTitle(title);
		initSubFormInterface();
		setLocation(500, 100);
		setDefaultCloseOperation(2);
		setModal(true);
	}

	private void initSubFormInterface() {
		this.formInterface.setEnabled(false);
		this.mainPane = getRootPane();
		this.mainPane.setLayout(new BorderLayout());
		String name = this.subformController.getName();

		this.panel = new JPanel();
		this.panel.setVisible(true);

		this.panelBotonesModificarRestoCampos = new SubFormOtherFieldsButtonsPanel(this);
		this.panelBotones = new SubFormButtonsPanel(this);

		this.mainPane.add(this.panel, "Center");
		this.mainPane.add(this.panelBotones, "South");

		setSize(500, 650);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				SubFormInterface.this.formInterface.setEnabled(true);
				SubFormInterface.this.showInterface(false);
			}
		});
	}

	public void setSeleccionVarios(boolean seleccionVarios)
	{
		this.seleccionVarios = seleccionVarios;
	}

	public void setProcesado(boolean procesado) {
		this.procesado = procesado;
	}

	public boolean getProcesado() {
		return this.procesado;
	}

	public SubForm getSubForm() {
		return this.subform;
	}

	public void addPanelTipos(FieldInterface field)
	{
		this.primaryField = field;
		this.formInterface.setEnabled(false);
		this.panelTipos = new SubFormListsPanel(this.subformController, field, this.claveForanea);
		this.panelTipos.setEnabled(true);
		this.panelTipos.habilitarBotones();
		this.datos = this.panelTipos.obtenerDatosIniciales(this.subform, this.datos, this.primaryField.getField().getName());
		this.panel.add(this.panelTipos, "South");
		this.mainPane.add(this.panel, "Center");
	}

	public boolean getAsignar() {
		return this.asignar;
	}

	public void cerrarDialogo() {
		this.dialog.setVisible(false);
		this.dialog.dispose();
		this.asignar = false;
		this.procesado = true;
	}

	public void confirmEdition()
	{
		this.panelTipos.borrarDatos();

		this.panelTipos.insertarAsignados(this.datos);

		this.panelTipos.reasignarDominiosIniciales();
		this.panelBotones.setVisible(true);

	}

	public void addDatos(OtrosDatosVO otroDato)
	{
		boolean encontrado = false;

		for (int i = 0; i < this.datos.size(); ++i) {
			OtrosDatosVO otrosDatosAux = (OtrosDatosVO)this.datos.get(i);
			if (otrosDatosAux.getCodigo().equals(otroDato.getCodigo())) {
				this.datos.remove(i);
			}

		}

		this.datos.add(otroDato);
	}

	public void eliminarDatos(String nombre)
	{
		for (int i = 0; i < this.datos.size(); ++i) {
			OtrosDatosVO otrosDatos = (OtrosDatosVO)this.datos.get(i);
			if (otrosDatos.getCodigo().equals(nombre)) {
				this.datos.remove(i);
			}
		}
	}

	public void insertarDatosParaDisponibles(Vector items)
	{
		this.subformPanel = new SubFormOtherFieldsPanel((UserDomain)this.panelTipos.getDomain(), this, this.subform, this.item, this.primaryField, this.claveForanea, true);

		this.dialog = new JDialog();

		this.dialog.setTitle("Datos del nivel: " + this.item);
		this.dialog.add(this.subformPanel, "Center");
		this.dialog.setSize(500, 250);
		this.dialog.setVisible(true);
		this.dialog.show();
		this.dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				SubFormInterface.this.dialog.setVisible(false);
				SubFormInterface.this.dialog.dispose();
			}
		});
	}

	public void insertarDatosParaDisponibles(String item)
	{
		if (this.subform.getFields().size() != 0) {
			this.subformPanel = new SubFormOtherFieldsPanel((UserDomain)this.panelTipos.getDomain(), this, this.subform, item, this.primaryField, this.claveForanea, true);

			this.dialog = new JDialog();
			this.dialog.add(this.panelBotonesModificarRestoCampos, "South");

			this.dialog.setTitle("Datos del nivel: " + item);

			this.dialog.add(this.subformPanel, "Center");
			setModal(false);
			this.dialog.setModal(true);
			this.dialog.setSize(500, 250);
			this.dialog.setVisible(true);
			this.dialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					SubFormInterface.this.dialog.setVisible(false);
					SubFormInterface.this.dialog.dispose();
					setModal(true);
				}

			});
		}
		else
		{
			aceptarModificacion();
		}
	}

	public void mostrarDatos(boolean editable)
	{
		this.item = (String)this.panelTipos.getJListAsignados().getSelectedValue();
		if (this.subform.getFields().size() == 0 ||
				this.item == null) {
			return;
		}
		this.subformPanel = new SubFormOtherFieldsPanel((UserDomain)this.panelTipos.getDomain(), this, this.subform, this.item, this.primaryField, this.claveForanea, editable);
		this.dialog = new JDialog();

		this.dialog.add(this.panelBotonesModificarRestoCampos, "South");

		this.dialog.setTitle("Datos del nivel: " + this.item);

		this.dialog.add(this.subformPanel, "Center");
		setModal(false);
		this.dialog.setSize(500, 250);
		this.dialog.setModal(true);
		this.dialog.setVisible(true);
		setModal(true);


	}

	public void setItem(String nombre)
	{
		this.item = nombre;
	}

	public String getItem() {
		return this.item;
	}

	public void aceptarModificacion()
	{
		OtrosDatosVO otrosDatos = new OtrosDatosVO();
		Vector datosHash = new Vector();

		otrosDatos.setCodigo(this.item);

		for (int i = 0; i < this.subform.getFields().size(); ++i)
		{
			FieldInterface field = (FieldInterface)this.subform.getFields().get(i);
			field.fillField();
			FieldController fieldControl = field.getField();

			CampoVO campo = new CampoVO();
			campo.setNombre(fieldControl.getName());
			campo.setValor(fieldControl.getValue());
			datosHash.add(campo);
		}

		otrosDatos.setDatos(datosHash);

		addDatos(otrosDatos);

		if (this.dialog != null) {
			this.dialog.setVisible(false);
			this.dialog.dispose();
		}
		this.asignar = true;
		this.procesado = true;
	}

	public void showInterface(boolean visible)
	{
		this.formInterface.setEnabled(true);
		setVisible(visible);
	}

	public SubFormController getSubFormController()
	{
		return this.subformController;
	}

	public void addClaveForanea()
	{
		JPanel panelClaveForanea = new JPanel();
		panelClaveForanea.setLayout(new GridBagLayout());
		this.gridbagconst2 = new GridBagConstraints();
		this.gridbagconst2.gridx = 0;
		this.gridbagconst.gridy = 0;
		this.dependencies = this.formInterface.getDependencies();
		this.groups = this.formInterface.getGroups();

		this.claveForanea = new ForeignKeyVO();
		Vector campos = new Vector();
		panelClaveForanea.setBorder(new TitledBorder(this.formInterface.getTitle()));

		for (int i = 0; i < this.subform.getForeignKey().size(); ++i) {
			String claveForanea = (String)this.subform.getForeignKey().get(i);
			CampoVO campo = new CampoVO();
			for (int j = 0; j < this.formInterface.getFormController().getFields().size(); ++j) {
				FieldController field = (FieldController)this.formInterface.getFormController().getFields().get(j);
				FieldInterface fieldInterface = (FieldInterface)this.formInterface.getFormController().getFieldsInterface().get(j);

				if (claveForanea.equals(field.getName())) {
					campo.setNombre(claveForanea);
					campo.setValor(field.getValue());
					campos.add(campo);
					FieldInterface clonFieldInterface = fieldInterface.clonar();
					clonFieldInterface.loadValue();

					this.gridbagconst2 = new GridBagConstraints();
					this.gridbagconst2.gridx = 0;

					panelClaveForanea.add(clonFieldInterface.getLabel(), this.gridbagconst2);
					this.gridbagconst2.gridx = 1;

					panelClaveForanea.add(clonFieldInterface.getComponent(), this.gridbagconst2);
					this.gridbagconst2.gridy += 1;
					break;
				}
			}

		}

		this.claveForanea.setCampos(campos);

		panelClaveForanea.setVisible(true);
		this.panel.add(panelClaveForanea, "North");

		this.panel.setVisible(true);
	}
}
