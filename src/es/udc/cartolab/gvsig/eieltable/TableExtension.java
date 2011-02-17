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

package es.udc.cartolab.gvsig.eieltable;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.extensionPoints.ExtensionPoints;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;

import es.udc.cartolab.gvsig.eieltable.gui.EIELNavTable;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;
import es.udc.cartolab.gvsig.navtable.NavTable;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class TableExtension extends Extension {

	public void initialize() {
		registerIcons();

		ExtensionPoints extensionPoints = ExtensionPointsSingleton.getInstance();
		extensionPoints.add("View_TocActions", "EIELForm", new TableTOCMenuEntry());

	}

	private void registerIcons() {
		PluginServices.getIconTheme().registerDefault(
				"eielform",
				this.getClass().getClassLoader().getResource("images/form.png")
		);
	}

	public void execute(String actionCommand) {

		View v = (View) PluginServices.getMDIManager().getActiveWindow();
		FLyrVect l = (FLyrVect) v.getMapControl().getMapContext().getLayers().getActives()[0];
		AbstractNavTable nt = new EIELNavTable(l);
		if (nt.init()) {
			((EIELNavTable)nt).open();
		} else {
			nt = new NavTable(l);
			if (nt.init()) {
				PluginServices.getMDIManager().addCentredWindow(nt);
			}
		}
	}

	public boolean isEnabled() {
		View v = (View) PluginServices.getMDIManager().getActiveWindow();
		FLayers layers = v.getMapControl().getMapContext().getLayers();
		if (DBSession.getCurrentSession() == null){
			return false;
		}
		else if (layers.getActives().length > 0) {
			return layers.getActives()[0] instanceof FLyrVect && !layers.getActives()[0].isEditing();
		} else {
			return false;
		}
	}

	public boolean isVisible() {
		return PluginServices.getMDIManager().getActiveWindow() instanceof View;
	}
}
