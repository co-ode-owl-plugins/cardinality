package org.coode.cardinality.action;

import org.coode.cardinality.model.CardinalityTableModel;
import org.coode.cardinality.ui.CardinalityTable;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.OWLIcons;
import org.protege.editor.owl.ui.UIHelper;
import org.protege.editor.owl.ui.view.OWLSelectionViewAction;
import org.semanticweb.owl.model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;

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
    private OWLModelManager mngr;
    private UIHelper uihelper;

    private OWLRestriction restr;

    public AddRowAction(CardinalityTable table, OWLEditorKit eKit) {
        super("Add row", OWLIcons.getIcon("class.add.png"));
        this.table = table;
        this.mngr = eKit.getOWLModelManager();
        uihelper = new UIHelper(eKit);
    }

    public void updateState() {
        setEnabled(true);
    }

    public void dispose() {
    }

    public void actionPerformed(ActionEvent actionEvent) {
        OWLObjectProperty prop = uihelper.pickOWLObjectProperty();
        if (prop != null) {
            OWLClass cls = uihelper.pickOWLClass();
            if (cls != null) {
                OWLOntology ontology = mngr.getActiveOntology();
                OWLDataFactory df = mngr.getOWLDataFactory();
                restr = df.getOWLObjectSomeRestriction(prop, cls);

                mngr.applyChange(new AddAxiom(ontology, df.getOWLSubClassAxiom(table.getSubject(), restr)));
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        CardinalityTableModel model = table.getModel();
                        int row = model.getRow(restr);
                        if (row >= 0) {
                            table.getSelectionModel().setSelectionInterval(row, row);
                        }
                    }
                });
            }
        }
    }
}
