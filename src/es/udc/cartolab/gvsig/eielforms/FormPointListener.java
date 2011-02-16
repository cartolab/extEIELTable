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

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.SingletonDialogAlreadyShownException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.PointSelectionListener;
import com.iver.cit.gvsig.fmap.tools.Events.PointEvent;

import es.udc.cartolab.gvsig.eielforms.gui.EIELNavTable;

public class FormPointListener extends PointSelectionListener {


	public FormPointListener(MapControl mc) {
		super(mc);
	}

	/**
	 * The image to display when the cursor is active.
	 */
	private final Image img = PluginServices.getIconTheme().get("cursor-query-information").getImage();

	/**
	 * The cursor used to work with this tool listener.
	 *
	 * @see #getCursor()
	 */
	private Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(img,
			new Point(16, 16), "");


	@Override
	public Cursor getCursor() {
		return cur;
	}

	@Override
	public void point(PointEvent event) throws BehaviorException {

		super.point(event);

		FLyrVect l = (FLyrVect) mapCtrl.getMapContext().getLayers().getActives()[0];
		try {
			if (l.getRecordset().getSelection().cardinality() > 0) {
				EIELNavTable nt = new EIELNavTable(l);
				if (nt.init()) {
					PluginServices.getMDIManager().addCentredWindow(nt);
					nt.onlySelected();
					nt.first();
				}
			}
		} catch (ReadDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SingletonDialogAlreadyShownException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	@Override
	public void pointDoubleClick(PointEvent event) throws BehaviorException {

	}

}
