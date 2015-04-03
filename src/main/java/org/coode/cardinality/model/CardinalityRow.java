package org.coode.cardinality.model;

import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLProperty;

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
 * When using the term "Restriction" we mean any OWL restriction OR any negated object some restriction
 */
public interface CardinalityRow extends Comparable<CardinalityRow>, Mergeable<CardinalityRow> {

    int NO_VALUE = -1;

    void setFactory(CardinalityRowFactory model);

    void addRestriction(OWLClassExpression restr, boolean readOnly);

    List<OWLOntologyChange> getDeleteChanges();

    List<OWLOntologyChange> getChanges();

//////////////// getters

    OWLClass getSubject();

    OWLProperty getProperty();

    OWLObject getFiller();

    int getMin();

    int getMax();

    boolean isClosed();

    boolean isReadOnly();

    boolean contains(OWLClassExpression restr);

    Set<OWLClassExpression> getEditableRestrictions();

    Set<OWLClassExpression> getReadOnlyRestrictions();

//    boolean isMinReadOnly();
//
//    boolean isMaxReadOnly();

///////////////// setters (should not commit changes to the model, but return the change objects

    void setProperty(OWLProperty property);

    void setFiller(OWLObject filler);

    void setMin(int newMin);

    void setMax(int newMax);

    void setClosed(boolean closed);
}
