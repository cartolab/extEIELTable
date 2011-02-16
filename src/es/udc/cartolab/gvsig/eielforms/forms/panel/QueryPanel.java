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

package es.udc.cartolab.gvsig.eielforms.forms.panel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import es.udc.cartolab.gvsig.eielforms.forms.FormInterface;

public class QueryPanel extends JPanel
{
	private FormInterface formInterface;
	private JButton editButton;
	private JButton deleteButton;
	private JButton exitButton;

	public QueryPanel(FormInterface formInterface)
	{
		this.formInterface = formInterface;

		this.editButton = new JButton("Editar Entidad");
		this.editButton.addActionListener(new InitEditionAction());

		this.deleteButton = new JButton("Eliminar Entidad");
		this.deleteButton.addActionListener(new InitDeletionAction());

		this.exitButton = new JButton("Salir");
		this.exitButton.addActionListener(new ExitAction());

		setLayout(new FlowLayout());
		add(this.editButton);

		add(this.exitButton);
		setVisible(true);
	}

	private class ExitAction
	implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			try
			{
				QueryPanel.this.formInterface.showInterface(false);
			}
			catch (Exception e) {
				System.out.println("Ocurrio algún error inesperado...");
				e.printStackTrace();
			}
		}
	}

	private class InitDeletionAction
	implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			try
			{
				QueryPanel.this.formInterface.startDeletion();
			}
			catch (Exception e) {
				System.out.println("Ocurrio algún error inesperado...");
				e.printStackTrace();
			}
		}
	}

	private class InitEditionAction
	implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			try
			{
				QueryPanel.this.formInterface.startEdition();
			}
			catch (Exception e) {
				System.out.println("Ocurrio algún error inesperado...");
				e.printStackTrace();
			}
		}
	}
}
