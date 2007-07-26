package org.coode.cardinality.test;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.coode.cardinality.model.CardinalityTableModel;
import org.protege.editor.owl.model.OWLModelManagerImpl;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLException;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Author: drummond<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 29, 2006<br><br>
 * <p/>
 * nick.drummond@cs.manchester.ac.uk<br>
 * www.cs.man.ac.uk/~drummond<br><br>
 */
public class TestCardinalityTableModel extends TestCase {

    OWLModelManager mngr;
    CardinalityTableModel model;

    private static final String BASE_URI = "http://www.co-ode.org/ontologies/pizza/2006/07/18/pizza.owl";

    void setup() {
        try {
            mngr = new OWLModelManagerImpl();
            mngr.loadOntology(new URI(BASE_URI));

            model = new CardinalityTableModel(mngr);
        }
        catch (OWLException e) {
            Logger.getLogger(TestCardinalityTableModel.class).error(e);
        }
        catch (URISyntaxException e) {
            Logger.getLogger(TestCardinalityTableModel.class).error(e);
        }
    }

    public void testFirst() {
        try {
            setup();
            OWLClass margherita = mngr.getOWLDataFactory().getOWLClass(new URI(BASE_URI + "#" + "MargheritaPizza"));
            model.setSubject(margherita);
            assert(model.getRowCount() == 2);
        }
        catch (Exception e) {
            Logger.getLogger(TestCardinalityTableModel.class).error(e);
        }
    }
}
