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
package es.udc.cartolab.gvsig.eielforms;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.plugins.ExtensionDecorator;
import com.iver.andami.ui.mdiFrame.JToolBarButton;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.listeners.CADListenerManager;
import com.iver.cit.gvsig.listeners.EndGeometryListener;

import es.udc.cartolab.gvsig.eielforms.gui.EIELNavTable;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;
import es.udc.cartolab.gvsig.navtable.AutoNavTableExtension;
import es.udc.cartolab.gvsig.navtable.NavTable;
import es.udc.cartolab.gvsig.navtable.ToggleEditing;

public class ThrowFormExtension extends Extension implements EndGeometryListener {

	//este valor se tomara desde las preferencias de NavTable y no en una variable aqui
	private boolean formsEnabled = false;
	private final URL offIcon = this.getClass().getClassLoader().getResource("images/forms.png");
	private final URL onIcon = this.getClass().getClassLoader().getResource("images/forms-active.png");
	private final String navTablePlugin = AutoNavTableExtension.KEY_NAME;

	
	public void initialize() {

		formsEnabled = AutoNavTableExtension.getPreferences();

		registerIcons();

	}

	private void registerIcons() {

		PluginServices.getIconTheme().registerDefault(
				"throw-form",
				this.getClass().getClassLoader().getResource("images/forms.png")
		);

	}

	
	public void execute(String actionCommand) {

		if (!formsEnabled) {
			CADListenerManager.addEndGeometryListener("es.udc.cartolab.eielforms", this);
			NotificationManager.addInfo("Formularios automáticos activados");
			formsEnabled = true;
			setIcon(onIcon, "Desactivar formularios automáticos");

		} else {
			CADListenerManager.removeEndGeometryListener("es.udc.cartolab.eielforms");
			NotificationManager.addInfo("Formularios automáticos desactivados");
			formsEnabled = false;
			setIcon(offIcon, "Activar formularios automáticos");
		}

		AutoNavTableExtension.savePreferences(formsEnabled);
	}

	private void setIcon(URL iconURL, String tooltip) {
		JToolBarButton throwFormButton = (JToolBarButton) PluginServices.getMainFrame().getComponentByName("throw-form");
		if (throwFormButton!=null) {
			Icon icon = new ImageIcon(iconURL);
			throwFormButton.setIcon(icon);
			throwFormButton.setToolTip(tooltip);
		}
	}

	
	public boolean isEnabled() {
		return true;
	}

	
	public boolean isVisible() {
		return true;
	}

	
	public void endGeometry(FLayer layer) {

		if (layer.isEditing()) {
			ToggleEditing te = new ToggleEditing();
			te.stopEditing(layer, false);
		}

		if (layer instanceof FLyrVect) {
			FLyrVect l = (FLyrVect) layer;
			AbstractNavTable nt = new EIELNavTable(l);
			if (nt.init()) {
				((EIELNavTable)nt).open();
			} else {
				nt = new NavTable(l);
				if (nt.init()) {
					PluginServices.getMDIManager().addCentredWindow(nt);
				}
			}
			nt.last();
		}
	}

	public void postInitialize() {
		//desactiva el plugin de opencadtools de navtable y quita el listener
		CADListenerManager.removeEndGeometryListener(navTablePlugin);
		PluginServices.getDecoratedExtension(es.udc.cartolab.gvsig.navtable.AutoNavTableExtension.class)
		.setVisibility(ExtensionDecorator.ALWAYS_INVISIBLE);

		URL icon;
		String tooltip;
		if (formsEnabled) {
			icon = onIcon;
			tooltip = "Desactivar formularios automáticos";
		} else {
			icon = offIcon;
			tooltip = "Activar formularios automáticos";
		}
		setIcon(icon, tooltip);
	}

}