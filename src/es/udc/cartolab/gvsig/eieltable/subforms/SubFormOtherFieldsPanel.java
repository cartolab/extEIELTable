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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;

import javax.swing.JPanel;

import es.udc.cartolab.gvsig.eieltable.domain.UserDomain;
import es.udc.cartolab.gvsig.eieltable.field.FieldController;
import es.udc.cartolab.gvsig.eieltable.field.FieldInterface;
import es.udc.cartolab.gvsig.eieltable.formgenerator.FormException;
import es.udc.cartolab.gvsig.eieltable.util.ForeignKeyVO;
import es.udc.cartolab.gvsig.eieltable.util.ObtenerDominioDAO;

public class SubFormOtherFieldsPanel extends JPanel
{
	private SubFormInterface subformInterfaz;
	private SubForm subform;
	private String item;
	private GridBagConstraints gridbagconst;
	private FieldInterface primaryField;
	private ForeignKeyVO claveForanea;
	private boolean editable;
	private UserDomain domain;

	public SubFormOtherFieldsPanel(UserDomain domain, SubFormInterface subformInterfaz, SubForm subform, String item, FieldInterface primaryField, ForeignKeyVO claveForanea, boolean editable)
	{
		this.claveForanea = claveForanea;
		this.subformInterfaz = subformInterfaz;
		this.primaryField = primaryField;
		this.subform = subform;
		this.item = item;
		this.editable = editable;
		this.domain = domain;

		initComponents();

		setLayout(new FlowLayout());

		setVisible(true); }

	public void initComponents() {
		String database = this.subformInterfaz.getSubFormController().getDataBase();
		String tabla = this.subformInterfaz.getSubFormController().getTable();
		ObtenerDominioDAO odDAO;
		try {
			odDAO = new ObtenerDominioDAO();

			for (int i = 0; i < this.subform.getFields().size(); ++i) {
				FieldInterface field = (FieldInterface)this.subform.getFields().get(i);
				FieldController fieldController = field.getField();
				String nombreCampoPrimario = this.primaryField.getField().getName();

				Object valor = odDAO.obtenerValorCampo(this.domain, this.item, fieldController.getName(), nombreCampoPrimario, database, tabla, this.claveForanea);
				if (valor == null) {
					valor = "";
				}
				fieldController.setValue(valor.toString());

				field.loadValue();
				field.getComponent().setEnabled(this.editable);

				this.gridbagconst = new GridBagConstraints();
				this.gridbagconst.gridx = 0;

				add(field.getLabel(), this.gridbagconst);
				this.gridbagconst.gridx = 1;

				add(field.getComponent(), this.gridbagconst);
			}
		} catch (FormException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
