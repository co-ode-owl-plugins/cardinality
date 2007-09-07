package org.coode.cardinality.model;

import org.coode.cardinality.util.ClosureUtils;
import org.coode.cardinality.util.RestrictionUtils;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.OWLObjectComparator;
import org.semanticweb.owl.model.*;

import javax.swing.*;
import java.util.*;

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
public class CardinalityRowImpl implements CardinalityRow {

    private static final String CLEAR_MAX_CONFIRM = "Transitive properties cannot have a max cardi of anything but 0. Clear the max value?";
    private static final String TRANS_MIN_CONFIRM = "Transitive properties cannot have a min cardi of anything but 0 or 1. Set the min value to 1?";

    protected final OWLClass subject;
    protected OWLObject filler; // cannot be immutable as may need to be specialised
    protected OWLProperty prop; // cannot be immutable as may need to be specialised
    protected int min;
    protected int max;
    private boolean closed;

    private final OWLObject originalFiller;
    private final OWLProperty originalProperty; // immutable
    private final boolean originallyClosed; // immutable

    protected final Collection<OWLDescription> editableRestrs = new ArrayList<OWLDescription>();

    protected final Collection<OWLDescription> readonlyRestrs = new ArrayList<OWLDescription>();

    protected final OWLModelManager mngr;

    private ClosureUtils closureUtils;

    private CardinalityRowFactory factory;

    protected final OWLObjectComparator<OWLObject> comparator;

    private boolean changed = false;

    public CardinalityRowImpl(OWLClass subject, OWLProperty objProp, OWLObject filler,
                              int min, int max, boolean closed, OWLModelManager mngr) {
        this.subject = subject;
        this.filler = filler;
        this.prop = objProp;
        this.min = min;
        this.max = max;
        this.closed = closed;

        this.originalFiller = filler;
        this.originalProperty = objProp;
        this.originallyClosed = closed;

        this.mngr = mngr;
        closureUtils = new ClosureUtils(mngr);
        this.comparator = new OWLObjectComparator<OWLObject>(mngr);
    }

    public void setFactory(CardinalityRowFactory factory) {
        this.factory = factory;
    }

    public void merge(CardinalityRow row) {
        int newMin = row.getMin();
        if ((min < 0) || (newMin > min)) {
            min = newMin;
        }

        int newMax = row.getMax();
        if ((newMax != RestrictionUtils.INFINITE) && ((max < 0) || (newMax <= max))) {
            max = newMax;
        }

        //@@TODO sort out filler and property merges based on subsumption

        readonlyRestrs.addAll(row.getReadOnlyRestrictions());
        editableRestrs.addAll(row.getEditableRestrictions());
    }

    public Set<OWLDescription> getEditableRestrictions() {
        return new HashSet<OWLDescription>(editableRestrs);
    }

    public Set<OWLDescription> getReadOnlyRestrictions() {
        return new HashSet<OWLDescription>(readonlyRestrs);
    }

    public void addRestriction(OWLDescription restr, boolean readOnly) {
        if (readOnly) {
            readonlyRestrs.add(restr);
        }
        else {
            editableRestrs.add(restr);
        }
    }

    public List<OWLOntologyChange> getDeleteChanges() {
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();

        final OWLDataFactory df = mngr.getOWLDataFactory();

        // removeRows all existing restrictions
        for (OWLDescription restr : editableRestrs) {
            final OWLSubClassAxiom owlSubClassAxiom = df.getOWLSubClassAxiom(subject, restr);
            changes.addAll(getDeleteAxiomFromAllOntologies(owlSubClassAxiom));
        }

        return changes;
    }

    public List<OWLOntologyChange> getChanges() {
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();

        if (changed) {

            final OWLDataFactory df = mngr.getOWLDataFactory();

            // removeRows all existing restrictions
            for (OWLDescription restr : editableRestrs) {
                OWLAxiom subclassAxiom = df.getOWLSubClassAxiom(subject, restr);
                changes.addAll(getDeleteAxiomFromAllOntologies(subclassAxiom));
            }

            // @@TODO should really regenerate them in the correct place
            OWLOntology ont = mngr.getActiveOntology();

            changes.addAll(CardinalityRowFactory.toOWL(this, ont, df));

            if (prop instanceof OWLObjectProperty){
                changes.addAll(updateClosure());
            }
        }
        return changes;
    }

    private List<OWLOntologyChange> updateClosure() {
        // in reality, only one feature gets updated at once, which should work fine
        // but this should also handle the cases where multiple features have been changed before a commit
        // this really needs testing for all combinations

        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();

        boolean createNewClosureOnNewProp = false;
        boolean removeOldClosureOnOldProp = false;
        boolean createNewClosureOnOldProp = false;
        boolean removeOldClosureOnNewProp = false;

        if (closed) {
            if (!originallyClosed) {
                removeOldClosureOnNewProp = true;
                createNewClosureOnNewProp = true;
            }

            if (filler != originalFiller) {
                removeOldClosureOnOldProp = true;
                createNewClosureOnNewProp = true;
            }

            if (prop != originalProperty) {
                removeOldClosureOnOldProp = true;
                removeOldClosureOnNewProp = true;
                createNewClosureOnOldProp = true;
                createNewClosureOnNewProp = true;
            }
        }
        else { // not closed
            if (originallyClosed) {
                removeOldClosureOnOldProp = true;
            }
            if (prop != originalProperty) {
                //removeOldClosureOnNewProp = true; // arguably the user should notice the highlighting
            }
        }

        OWLDataFactory df = mngr.getOWLDataFactory();

        if (removeOldClosureOnOldProp) { // removeRows old closure on old prop
            OWLObjectAllRestriction oldClosure = closureUtils.getClosureAxiom(subject, (OWLObjectProperty)originalProperty, originalFiller);
            if (oldClosure != null) {
                OWLAxiom subclassAxiom = df.getOWLSubClassAxiom(subject, oldClosure);
                changes.addAll(getDeleteAxiomFromAllOntologies(subclassAxiom));
            }
        }

        if (removeOldClosureOnNewProp) { // removeRows old closure axioms along new prop
            for (OWLObjectAllRestriction closure : closureUtils.getCandidateClosureAxioms(subject, (OWLObjectProperty)prop)) {
                OWLAxiom subclassAxiom = df.getOWLSubClassAxiom(subject, closure);
                changes.addAll(getDeleteAxiomFromAllOntologies(subclassAxiom));
            }
        }

        if (createNewClosureOnNewProp) { // create new closure on new prop
            Set<OWLDescription> fillers = factory.getFillers((OWLObjectProperty)prop);
            if (!fillers.isEmpty()) {
                OWLObjectAllRestriction newClosure = closureUtils.createClosureAxiom((OWLObjectProperty)prop, fillers);
                changes.add(new AddAxiom(mngr.getActiveOntology(), df.getOWLSubClassAxiom(subject, newClosure)));
            }
        }

        if (createNewClosureOnOldProp) { // create new closure on old prop
            Set<OWLDescription> fillers = factory.getFillers((OWLObjectProperty)originalProperty);
            if (!fillers.isEmpty()) {
                OWLObjectAllRestriction newClosure = closureUtils.createClosureAxiom((OWLObjectProperty)originalProperty, fillers);
                changes.add(new AddAxiom(mngr.getActiveOntology(), df.getOWLSubClassAxiom(subject, newClosure)));
            }
        }
        return changes;
    }

    public OWLClass getSubject() {
        return subject;
    }

    public OWLProperty getProperty() {
        return prop;
    }

    public OWLObject getFiller() {
        if (prop instanceof OWLDataProperty){
            System.out.println("filler = " + filler);
        }
        return filler;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean isReadOnly() {
        return editableRestrs.isEmpty();
    }

    public boolean contains(OWLDescription restr) {
        return editableRestrs.contains(restr) || readonlyRestrs.contains(restr);
    }

    public void setProperty(OWLProperty property) {
        if (property != prop) {
            boolean update = true;

            if (isTransitive(property)) {
                if (max > 0) { // warn that this must be cleared for transitive properties
                    switch (JOptionPane.showConfirmDialog(null, CLEAR_MAX_CONFIRM)) {
                        case JOptionPane.YES_OPTION:
                            setMax(CardinalityRow.NO_VALUE);
                            break;
                        case JOptionPane.CANCEL_OPTION:
                            update = false;
                    }
                }
                if (update && min > 1) { // warn that this must be cleared for transitive properties
                    switch (JOptionPane.showConfirmDialog(null, TRANS_MIN_CONFIRM)) {
                        case JOptionPane.YES_OPTION:
                            setMin(1);
                            break;
                        case JOptionPane.CANCEL_OPTION:
                            update = false;
                    }
                }
            }
            if (update) {
                this.prop = property;
                changed = true;
            }
        }
    }

    public void setFiller(OWLObject filler) {
        this.filler = filler;
        changed = true;
    }

    public void setMin(int newMin) {

        boolean update = true;

        if (isTransitive(prop)) {
            if (newMin > 1) { // warn that max is 1 for transitive properties
                switch (JOptionPane.showConfirmDialog(null, TRANS_MIN_CONFIRM)) {
                    case JOptionPane.YES_OPTION:
                        newMin = 1;
                        break;
                    case JOptionPane.NO_OPTION:
                        // just allow the change to happen
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        update = false;
                }
            }
        }

        if (update && newMin != min) {
            this.min = newMin;
            changed = true;
        }
    }

    public void setMax(int newMax) {

        boolean update = true;

        if (isTransitive(prop)) {
            if (newMax > 0) { // warn that this must be cleared for transitive properties
                switch (JOptionPane.showConfirmDialog(null, CLEAR_MAX_CONFIRM)) {
                    case JOptionPane.YES_OPTION:
                        newMax = CardinalityRow.NO_VALUE;
                        update = (max != newMax);
                        break;
                    case JOptionPane.NO_OPTION:
                        // just allow the change to happen
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        update = false;
                }
            }
        }

        if (update && newMax != max) {
            this.max = newMax;
            changed = true;
        }
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
        changed = true;
    }

    public int compareTo(Object o) {
        int result;

        CardinalityRow other = (CardinalityRow) o;

        Boolean thisReadOnly = isReadOnly();
        Boolean otherReadOnly = other.isReadOnly();
        result = thisReadOnly.compareTo(otherReadOnly);

        if (result == 0) {
            result = comparator.compare(prop, other.getProperty());
            if (result == 0) {
                result = comparator.compare(filler, other.getFiller());
            }
        }

        return result;
    }


    private boolean isTransitive(OWLProperty property) {
        if (property instanceof OWLObjectProperty){
            for (OWLOntology ont : mngr.getActiveOntologies()){
                if (((OWLObjectProperty)property).isTransitive(ont)){
                    return true;
                }
            }
        }
        return false;
    }

    private List<OWLOntologyChange> getDeleteAxiomFromAllOntologies(OWLAxiom axiom){
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        for (OWLOntology ont : mngr.getActiveOntologies()){
            if (ont.containsAxiom(axiom)){
                changes.add(new RemoveAxiom(ont, axiom));
            }
        }
        return changes;
    }
}
