package org.coode.cardinality.test;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.coode.cardinality.model.CardinalityTableModel;
import org.protege.editor.owl.model.OWLModelManagerImpl;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;

import java.net.URI;
import java.net.URISyntaxException;

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
public class TestCardinalityTableModel extends TestCase {

    OWLModelManagerImpl mngr;
    CardinalityTableModel model;

    private static final String BASE_URI = "http://www.co-ode.org/ontologies/pizza/2006/07/18/pizza.owl";

    void setup() {
        try {
            mngr = new OWLModelManagerImpl();
            mngr.loadOntologyFromPhysicalURI(new URI(BASE_URI));

            model = new CardinalityTableModel(mngr);
        }
        catch (URISyntaxException e) {
            Logger.getLogger(TestCardinalityTableModel.class).error(e);
        }
    }

    public void testFirst() {
        try {
            setup();
            OWLClass margherita = mngr.getOWLDataFactory().getOWLClass(IRI.create(BASE_URI + "#" + "MargheritaPizza"));
            model.setSubject(margherita);
            assert(model.getRowCount() == 2);
        }
        catch (Exception e) {
            Logger.getLogger(TestCardinalityTableModel.class).error(e);
        }
    }
}
