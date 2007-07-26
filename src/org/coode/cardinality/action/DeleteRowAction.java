package org.coode.cardinality.action;

import org.coode.cardinality.model.CardinalityRow;
import org.coode.cardinality.ui.CardinalityTable;
import org.protege.editor.owl.ui.OWLIcons;
import org.protege.editor.owl.ui.view.OWLSelectionViewAction;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.util.Collection;

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
public class DeleteRowAction extends OWLSelectionViewAction {

    private CardinalityTable table;

    private ListSelectionListener l = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            updateState();
        }
    };

    public DeleteRowAction(CardinalityTable table) {
        super("Delete row", OWLIcons.getIcon("class.delete.png"));
        this.table = table;
        table.getSelectionModel().addListSelectionListener(l);
    }

    public void updateState() {
        boolean canDelete = false;
        Collection<CardinalityRow> restrs = table.getSelection();
        for (CardinalityRow restr : restrs) {
            if (!restr.isReadOnly()) {
                canDelete = true;
            }
        }
        setEnabled(canDelete);
    }

    public void dispose() {
        table.getSelectionModel().removeListSelectionListener(l);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        table.getModel().removeRestrictions(table.getSelection());
    }
}
