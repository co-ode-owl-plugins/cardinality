package org.coode.cardinality.ui;

import org.coode.cardinality.prefs.CardinalityPreferences;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.ui.OWLEntityComparator;
import org.protege.editor.owl.ui.renderer.OWLCellRenderer;
import org.semanticweb.owl.model.*;

import javax.swing.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: drummond<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 25, 2006<br><br>
 * <p/>
 * nick.drummond@cs.manchester.ac.uk<br>
 * www.cs.man.ac.uk/~drummond<br><br>
 */
public class PropCombo extends JComboBox {

    private OWLModelManager mngr;

    private boolean changesMade = false;

    private OWLEntityComparator<OWLProperty> comp;

    private OWLOntologyChangeListener ontologyChangeListener = new OWLOntologyChangeListener() {
        public void ontologiesChanged(List<? extends OWLOntologyChange> list) throws OWLException {
            for (OWLOntologyChange ontologyChange : list){
                final OWLAxiom axiom = ontologyChange.getAxiom();
                if (axiom instanceof OWLDeclarationAxiom &&
                        ((OWLDeclarationAxiom)axiom).getEntity() instanceof OWLProperty){
                    changesMade = true;
                }
            }
        }
    };

    // listen to whether the currently active ontology has changed
    private OWLModelManagerListener activeOntologyListener = new OWLModelManagerListener() {
        public void handleChange(OWLModelManagerChangeEvent event) {
            if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED ||
                event.getType() == EventType.ONTOLOGY_VISIBILITY_CHANGED) {
                reload();
            }
        }
    };

    public PropCombo(OWLEditorKit eKit) {
        super();

        mngr = eKit.getOWLModelManager();

        comp = new OWLEntityComparator<OWLProperty>(mngr);

        setRenderer(new OWLCellRenderer(eKit));

        // deal with adding and removing object properties
        mngr.addOntologyChangeListener(ontologyChangeListener);

        mngr.addListener(activeOntologyListener);

        load();
    }

    public void setPopupVisible(boolean v) {
        if (changesMade) {
            reload();
            changesMade = false;
        }
        super.setPopupVisible(v);
    }

    private void reload() {
        removeAllItems();
        load();
    }

    private void load() {
        List<OWLProperty> props = new LinkedList<OWLProperty>(mngr.getActiveOntology().getReferencedObjectProperties());
        if (CardinalityPreferences.getInstance().getBoolean(CardinalityPreferences.OPT_EDIT_DT_PROPERTIES, false)) {
            props.addAll(mngr.getActiveOntology().getReferencedDataProperties());
        }
        Collections.sort(props, comp);
        addItems(props);
    }

    private void addItems(List<? extends OWLProperty> properties) {
        if (properties != null) {
            for (OWLProperty prop : properties) {
                    addItem(prop);
            }
        }
    }

    protected void finalize() throws Throwable {
        mngr.removeOntologyChangeListener(ontologyChangeListener);
        mngr.removeListener(activeOntologyListener);
        mngr = null;
        super.finalize();
    }
}
