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

import java.util.HashMap;

import com.iver.andami.plugins.Extension;

import es.udc.cartolab.gvsig.eieltable.gui.AlphanumericForm;
import es.udc.cartolab.gvsig.eielutils.constants.Constants;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class AlphanumericTableExtension extends Extension {

	private HashMap<String, Integer> formHeights = new HashMap<String,Integer>();


	public void initialize() {

	}


	public void execute(String actionCommand) {
		AlphanumericForm af = new AlphanumericForm(actionCommand);
		af.open();

		Integer height = formHeights.get(actionCommand);
		if (height!=null) {
			af.setHeight(height);
		}

	}


	public boolean isEnabled() {
		return true;
	}


	public boolean isVisible() {
		DBSession dbs = DBSession.getCurrentSession();
		Constants cts = Constants.getCurrentConstants();
		return dbs!=null && cts.getNucCod()!=null;
	}

}