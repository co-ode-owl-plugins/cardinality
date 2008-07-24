package org.coode.cardinality.ui.celleditor;

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
public class PropCombo extends JComboBox {

    private OWLModelManager mngr;

    private boolean changesMade = false;

    private OWLEntityComparator<OWLProperty> comp;

    public static final int OBJ_PROPS = 0;
    public static final int DATA_PROPS = 1;

    private int type;

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

    public PropCombo(OWLEditorKit eKit, int type) {
        super();

        this.type = type;

        mngr = eKit.getModelManager();

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
        List<OWLProperty> props;
        if (type == OBJ_PROPS){
            props = new LinkedList<OWLProperty>(mngr.getActiveOntology().getReferencedObjectProperties());
        }
        else{
            props = new LinkedList<OWLProperty>(mngr.getActiveOntology().getReferencedDataProperties());
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

    public void dispose() {
        mngr.removeOntologyChangeListener(ontologyChangeListener);
        mngr.removeListener(activeOntologyListener);
        ontologyChangeListener = null;
        activeOntologyListener = null;
        mngr = null;
    }
}
