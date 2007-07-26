package org.coode.cardinality.model;

import org.semanticweb.owl.model.*;

import java.util.List;
import java.util.Set;

/**
 * Author: drummond<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 25, 2006<br><br>
 * <p/>
 * nick.drummond@cs.manchester.ac.uk<br>
 * www.cs.man.ac.uk/~drummond<br><br>
 * <p/>
 * When using the term "Restriction" we mean any OWL restriction OR any negated object some restriction
 */
public interface CardinalityRow extends Comparable {

    int NO_VALUE = -1;

    void setModel(CardinalityTableModel model);

    void merge(CardinalityRow row);

    void addRestriction(OWLDescription restr, boolean readOnly);

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

    boolean contains(OWLDescription restr);

    Set<OWLDescription> getEditableRestrictions();

    Set<OWLDescription> getReadOnlyRestrictions();

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
