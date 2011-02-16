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

package es.udc.cartolab.gvsig.eieltable.forms.panel;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JPanel;

import es.udc.cartolab.gvsig.eieltable.forms.FormInterface;

public class SubFormButtonPanel extends JPanel
{
  private FormInterface formInterfaz;
  private JButton subformButton;
  private String nombre;
  private Vector subformsButtons = new Vector();

  public SubFormButtonPanel(FormInterface formInterfaz) {
    this.formInterfaz = formInterfaz;

    setLayout(new FlowLayout());
    setVisible(true);
  }

  public void addButton(String nombreSubform)
  {
    this.nombre = nombreSubform;
    JButton button = new JButton(nombreSubform);
    button.addMouseListener(new MiMouseAdapter(nombreSubform) {
      public void mouseClicked(MouseEvent evt) {
        SubFormButtonPanel.this.jButtonAbrirSubformularioMouseClicked(evt, getNombre());
      }
    });
    setLayout(new FlowLayout());
    add(button, 0);
    this.subformsButtons.add(button);
    setVisible(true);
  }

  private void jButtonAbrirSubformularioMouseClicked(MouseEvent evt, String nombre)
  {
    this.formInterfaz.openSubForm(nombre);
  }

  private class MiMouseAdapter extends MouseAdapter
  {
    private String nombre;

    public MiMouseAdapter(String paramString)
    {
      this.nombre = paramString; }

    public String getNombre() {
      return this.nombre;
    }
  }
}
