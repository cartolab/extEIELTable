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

package es.udc.cartolab.gvsig.eielforms.subforms;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class SubFormButtonsPanel extends JPanel
{
	private SubFormInterface subformInterface;
	private JButton saveButton;
	private JButton deleteButton;
	private JButton exitButton;
	private JButton datosButton;

	public SubFormButtonsPanel(SubFormInterface subformInterface)
	{
		this.subformInterface = subformInterface;

		this.saveButton = new JButton("Aceptar");
		this.saveButton.addActionListener(new InitEditionAction());

		this.exitButton = new JButton("Cancelar");
		this.exitButton.addActionListener(new ExitAction());

		this.datosButton = new JButton("Datos");
		this.datosButton.addActionListener(new DatosAction());

		setLayout(new FlowLayout());
		add(this.datosButton);
		add(this.saveButton);
		add(this.exitButton);
		setVisible(true);
	}

	private class DatosAction
	implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			try
			{
				SubFormButtonsPanel.this.subformInterface.mostrarDatos(false);
			}
			catch (Exception e) {
				System.out.println("Ocurrio algún error inesperado...");
				e.printStackTrace();
			}
		}
	}

	private class ExitAction
	implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			try
			{
				SubFormButtonsPanel.this.subformInterface.showInterface(false);
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
				//        PanelBotones.this.subformInterface.startEdition();
				subformInterface.confirmEdition();
				SubFormButtonsPanel.this.subformInterface.showInterface(false);
			}
			catch (Exception e) {
				System.out.println("Ocurrio algún error inesperado...");
				e.printStackTrace();
			}
		}
	}
}
