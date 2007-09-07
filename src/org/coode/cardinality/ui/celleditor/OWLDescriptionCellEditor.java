package org.coode.cardinality.ui.celleditor;

import org.apache.log4j.Logger;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.description.OWLDescriptionParser;
import org.protege.editor.owl.ui.clsdescriptioneditor.ExpressionEditor;
import org.protege.editor.owl.ui.clsdescriptioneditor.OWLDescriptionChecker;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLException;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/*
 * Copyright (C) 2007, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/**
 * Author: Nick Drummond<br>
 * nick.drummond@cs.manchester.ac.uk<br>
 * http://www.cs.man.ac.uk/~drummond<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Aug 30, 2006<br><br>
 * <p/>
 */
public class OWLDescriptionCellEditor extends AbstractCellEditor implements TableCellEditor {

    private OWLModelManager owlModelManager;

    private ExpressionEditor owlDescriptionEditor;

    private boolean expandable;

    public static final int EXPANDABLE_ROW_HEIGHT = 45;

    public OWLDescriptionCellEditor(OWLEditorKit eKit) {
        this.owlModelManager = eKit.getOWLModelManager();
        owlDescriptionEditor = new ExpressionEditor<OWLDescription>(eKit, new OWLDescriptionChecker(eKit));
        owlDescriptionEditor.setOpaque(false);
        owlDescriptionEditor.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!e.isConsumed()) {
                        e.consume();
                        fireEditingStopped();
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (!e.isConsumed()) {
                        e.consume();
                        fireEditingCanceled();
                    }
                }
            }
        });
        expandable = false;
        owlDescriptionEditor.setFont(owlDescriptionEditor.getFont().deriveFont(Font.PLAIN, 12.0f));
    }

    /**
     * Makes the cell clsdescriptioneditor expandable.  If the cell clsdescriptioneditor is
     * expandable, then the the table
     * row that contains the cell being edited is expanded to give
     * the user a few lines to edit the expression.
     * @param expandable
     */
    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public void setEditorIndentation(int editorIndentation) {
        owlDescriptionEditor.setBorder(BorderFactory.createEmptyBorder(0, editorIndentation, 0, 0));
    }

    public Object getCellEditorValue() {
        OWLDescriptionParser parser = owlModelManager.getOWLDescriptionParser();
        OWLDescription descr = null;
        try {
            descr = parser.createOWLDescription(owlDescriptionEditor.getText());
        }
        catch (OWLException e) {
            Logger.getLogger(OWLDescriptionCellEditor.class).error(e.getMessage());
        }
        return descr;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (expandable) {
            table.setRowHeight(row, EXPANDABLE_ROW_HEIGHT);
        }

        OWLDescription desc = (OWLDescription) value;
        if (desc != null) {
            owlDescriptionEditor.setText(owlModelManager.getOWLObjectRenderer().render(desc, owlModelManager.getOWLEntityRenderer()));
        }
        else {
            owlDescriptionEditor.setText("");
        }
        return owlDescriptionEditor;
    }

    public boolean isCellEditable(EventObject eventObject) {
        if (eventObject instanceof MouseEvent) {
            return ((MouseEvent) eventObject).getClickCount() >= 2;
        }
        else {
            return super.isCellEditable(eventObject);
        }
    }
}
