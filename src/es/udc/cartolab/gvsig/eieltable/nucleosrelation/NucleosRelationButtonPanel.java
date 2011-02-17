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

package es.udc.cartolab.gvsig.eieltable.nucleosrelation;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;

import es.udc.cartolab.gvsig.eieltable.formgenerator.FormException;
import es.udc.cartolab.gvsig.eieltable.forms.FormInterface;
import es.udc.cartolab.gvsig.eieltable.gui.NucleosRelationWindow;

public class NucleosRelationButtonPanel extends JPanel {

	FormInterface formInterface;


	public NucleosRelationButtonPanel(FormInterface formInterface) {
		this.formInterface = formInterface;
		addButton();
	}

	private void addButton() {
		JButton button = new JButton("Núcleos");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Map<String, String> fields = formInterface.getNucleosRelation().getFields();
				ArrayList<String> tableFieldNames = getTableFields(fields);

				HashMap<String, String> tableValues = formInterface.getFormController().getFieldValues(tableFieldNames);
				HashMap<String, String> relationValues = new HashMap<String,String>();

				for (String fieldName : tableFieldNames) {
					relationValues.put(fields.get(fieldName), tableValues.get(fieldName));
				}

				NucleosRelationWindow w;
				try {
					w = new NucleosRelationWindow(
							formInterface.getNucleosRelation().getTableName(),
							formInterface.getFormController().getName(),
							relationValues);
					w.open();
				} catch (FormException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			private ArrayList<String> getTableFields(Map<String, String> relationFieldsMap) {

				Set<String> keySet = relationFieldsMap.keySet();
				Iterator<String> iterator = keySet.iterator();
				ArrayList<String> tableFieldsNames = new ArrayList<String>();
				while (iterator.hasNext()) {
					tableFieldsNames.add(iterator.next());
				}

				return tableFieldsNames;
			}

		});
		setLayout(new FlowLayout());
		add(button, 0);
	}

}
