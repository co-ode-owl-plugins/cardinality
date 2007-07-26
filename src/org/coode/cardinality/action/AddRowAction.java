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

/**
 * Author: drummond<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 30, 2006<br><br>
 * <p/>
 * nick.drummond@cs.manchester.ac.uk<br>
 * www.cs.man.ac.uk/~drummond<br><br>
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
