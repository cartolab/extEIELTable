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

package es.udc.cartolab.gvsig.eieltable.forms.panel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import es.udc.cartolab.gvsig.eieltable.forms.FormInterface;

public class EditPanel extends JPanel
{
	private FormInterface formInterfaz;
	private JButton insertButton;
	private JButton cancelButton;

	public EditPanel(FormInterface formInterfaz)
	{
		this.formInterfaz = formInterfaz;

		this.insertButton = new JButton("Confirmar Edicion");
		this.insertButton.addActionListener(new ConfirmEditonAction());

		this.cancelButton = new JButton("Cancelar Edicion");
		this.cancelButton.addActionListener(new CancelEditionAction());

		setLayout(new FlowLayout());
		add(this.insertButton);
		add(this.cancelButton);
		setVisible(true);
	}

	private class CancelEditionAction
	implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			try
			{
				EditPanel.this.formInterfaz.cancelEdition();
			}
			catch (Exception e) {
				System.out.println("Ocurrio algún error inesperado...");
				e.printStackTrace();
			}
		}
	}

	private class ConfirmEditonAction
	implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			try
			{
				EditPanel.this.formInterfaz.confirmEdition();
			}
			catch (Exception e) {
				System.out.println("Ocurrio algún error inesperado...");
				e.printStackTrace();
			}
		}
	}
}
