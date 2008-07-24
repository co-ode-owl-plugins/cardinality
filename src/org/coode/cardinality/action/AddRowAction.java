package org.coode.cardinality.action;

import org.coode.cardinality.model.CardinalityRow;
import org.coode.cardinality.model.CardinalityRowFactory;
import org.coode.cardinality.model.CardinalityTableModel;
import org.coode.cardinality.ui.CardinalityTable;
import org.coode.cardinality.ui.roweditor.CardinalityRowEditor;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.OWLIcons;
import org.protege.editor.owl.ui.view.OWLSelectionViewAction;
import org.semanticweb.owl.model.OWLOntologyChange;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

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
public class AddRowAction extends OWLSelectionViewAction {

    private CardinalityTable table;

    private OWLEditorKit eKit;

    private CardinalityRowEditor editor;

    private CardinalityRow newRow;

    public AddRowAction(CardinalityTable table, OWLEditorKit eKit) {
        super("Add row", OWLIcons.getIcon("class.add.png"));
        this.table = table;
        this.eKit = eKit;
    }

    public void updateState() {
        setEnabled(true);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (editor == null){
            editor = new CardinalityRowEditor(eKit, table.getModel().getSubject());
        }

        if (editor.showDialog(table.getModel().getSubject()) == JOptionPane.OK_OPTION){
            OWLModelManager mngr = eKit.getModelManager();
            newRow = editor.createRow();
            if (newRow != null){
                List<OWLOntologyChange> changes = CardinalityRowFactory.toOWL(newRow,
                                                                              mngr.getActiveOntology(),
                                                                              mngr.getOWLDataFactory());
                mngr.applyChanges(changes);

                // select the new row if possible
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        CardinalityTableModel model = table.getModel();
                        int row = model.getRow(newRow);
                        if (row >= 0) {
                            table.getSelectionModel().setSelectionInterval(row, row);
                        }
                    }
                });
            }
        }
    }

    public void dispose() {
        editor.dispose();
    }
}