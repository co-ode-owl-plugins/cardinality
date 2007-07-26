package org.coode.cardinality.ui;

import org.coode.cardinality.action.AddRowAction;
import org.coode.cardinality.action.DeleteRowAction;
import org.protege.editor.owl.ui.view.AbstractOWLClassViewComponent;
import org.semanticweb.owl.model.OWLClass;

import javax.swing.*;
import java.awt.*;

/**
 * Author: drummond<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 25, 2006<br><br>
 * <p/>
 * nick.drummond@cs.manchester.ac.uk<br>
 * www.cs.man.ac.uk/~drummond<br><br>
 */
public class CardinalityView extends AbstractOWLClassViewComponent {

    private CardinalityTable table;

    private AddRowAction addRowAction;

    private DeleteRowAction deleteRowAction;

    public void initialiseClassView() throws Exception {
        setLayout(new BorderLayout(6, 6));

        table = new CardinalityTable(getOWLEditorKit());

        JScrollPane scroller = new JScrollPane(table);
        add(scroller, BorderLayout.CENTER);

        addRowAction = new AddRowAction(table, getOWLEditorKit());
        deleteRowAction = new DeleteRowAction(table);

        addAction(addRowAction, "A", "A");
        addAction(deleteRowAction, "A", "B");
    }

    protected OWLClass updateView(OWLClass selectedClass) {
        table.setSubject(selectedClass);
        return selectedClass;
    }

    public void disposeView() {
    }
}
