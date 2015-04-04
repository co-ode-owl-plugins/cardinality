package org.coode.cardinality.ui;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import org.coode.cardinality.action.AddRowAction;
import org.coode.cardinality.action.DeleteRowAction;
import org.protege.editor.owl.ui.view.cls.AbstractOWLClassViewComponent;
import org.semanticweb.owlapi.model.OWLClass;

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
 * http://www.cs.man.ac.uk/~drummond<br>
 * <br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Aug 30, 2006<br>
 * <br>
 * <p/>
 */
public class CardinalityView extends AbstractOWLClassViewComponent {
    private static final long serialVersionUID = 1L;

    private CardinalityTable table;
    private AddRowAction addRowAction;
    private DeleteRowAction deleteRowAction;

    @Override
    public void initialiseClassView() {
        setLayout(new BorderLayout(6, 6));
        table = new CardinalityTable(getOWLEditorKit());
        JScrollPane scroller = new JScrollPane(table);
        add(scroller, BorderLayout.CENTER);
        addRowAction = new AddRowAction(table, getOWLEditorKit());
        deleteRowAction = new DeleteRowAction(table);
        addAction(addRowAction, "A", "A");
        addAction(deleteRowAction, "A", "B");
    }

    @Override
    protected OWLClass updateView(OWLClass selectedClass) {
        table.clearSelection();
        table.getModel().setSubject(selectedClass);
        return selectedClass;
    }

    @Override
    public void disposeView() {
        table.dispose();
        addRowAction.dispose();
        deleteRowAction.dispose();
    }
}
