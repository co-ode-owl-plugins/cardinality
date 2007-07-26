package org.coode.cardinality.action;

import org.coode.cardinality.model.CardinalityRow;
import org.coode.cardinality.ui.CardinalityTable;
import org.protege.editor.owl.ui.OWLIcons;
import org.protege.editor.owl.ui.view.OWLSelectionViewAction;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.util.Collection;

/**
 * Author: drummond<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 30, 2006<br><br>
 * <p/>
 * nick.drummond@cs.manchester.ac.uk<br>
 * www.cs.man.ac.uk/~drummond<br><br>
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
