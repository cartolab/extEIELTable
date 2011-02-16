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

package es.udc.cartolab.gvsig.eieltable.groups;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class SinglePaneGroupInterface extends ComplexFieldGroup
{
	private GridBagConstraints gridbagconst;

	public SinglePaneGroupInterface(String groupName, String layout)
	{
		super(groupName, layout, new JPanel());
		configureLayout();
	}

	@Override
	public void addGroup(FieldGroup group) {
		this.groups.add(group);
		if (this.layout.compareTo("FLOWLAYOUT") == 0) {
			((JPanel)this.interfazGrupo).add(group.getInterface());
		}
		else if (this.layout.compareTo("GRIDBAGLAYOUT") == 0) {
			((JPanel)this.interfazGrupo).add(group.getInterface(), this.gridbagconst);
			refreshLayout();
		}
	}

	private void configureLayout()
	{
		if (this.layout.compareTo("FLOWLAYOUT") == 0) {
			((JPanel)this.interfazGrupo).setLayout(new FlowLayout());
		}
		else if (this.layout.compareTo("GRIDBAGLAYOUT") == 0) {
			((JPanel)this.interfazGrupo).setLayout(new GridBagLayout());
			this.gridbagconst = new GridBagConstraints();

			this.gridbagconst.gridy = 0;
		}
	}

	private void refreshLayout()
	{
		if (this.layout.compareTo("FLOWLAYOUT") == 0) {
			return;
		}
		if (this.layout.compareTo("GRIDBAGLAYOUT") == 0) {
			this.gridbagconst.gridy += 1;
		}
	}
}
