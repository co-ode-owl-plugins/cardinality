package org.coode.cardinality.util;

import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.OWLObjectVisitorAdapter;

import java.util.List;
import java.util.ArrayList;

/**
 * Author: drummond<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 29, 2006<br><br>
 * <p/>
 * nick.drummond@cs.manchester.ac.uk<br>
 * www.cs.man.ac.uk/~drummond<br><br>
 */
public class RestrictionPropertySetter extends OWLObjectVisitorAdapter {
//    private OWLOntology ont;
//    private OWLClass cls;
//    private OWLObjectProperty newProp;
//
//    private List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
//
//    public RestrictionPropertySetter(OWLOntology ont, OWLClass cls, OWLObjectProperty newProp) {
//        this.ont = ont;
//        this.cls = cls;
//        this.newProp = newProp;
//    }
//
//    public void visit(OWLObjectSomeRestriction owlRestr) throws OWLException {
//        changes.add(new RemoveSuperClass(ont, cls, owlRestr, null));
//        OWLDataFactory df = owlRestr.getOWLDataFactory();
//        OWLObjectSomeRestriction clone = df.getOWLObjectSomeRestriction(newProp,
//                                                                        owlRestr.getDescription());
//        changes.add(new AddSuperClass(ont, cls, clone, null));
//    }
//
//    public void visit(OWLObjectAllRestriction owlRestr) throws OWLException {
//        changes.add(new RemoveSuperClass(ont, cls, owlRestr, null));
//        OWLDataFactory df = owlRestr.getOWLDataFactory();
//        OWLObjectAllRestriction clone = df.getOWLObjectAllRestriction(newProp,
//                                                                      owlRestr.getDescription());
//        changes.add(new AddSuperClass(ont, cls, clone, null));
//    }
//
//    public void visit(OWLObjectValueRestriction owlRestr) throws OWLException {
//        changes.add(new RemoveSuperClass(ont, cls, owlRestr, null));
//        OWLDataFactory df = owlRestr.getOWLDataFactory();
//        OWLObjectValueRestriction clone = df.getOWLObjectValueRestriction(newProp,
//                                                                          owlRestr.getIndividual());
//        changes.add(new AddSuperClass(ont, cls, clone, null));
//    }
//
//    public void visit(OWLObjectCardinalityRestriction owlRestr) throws OWLException {
//
//        // @@TODO update qualified cardinality when OWL API updated
//
//        changes.add(new RemoveSuperClass(ont, cls, owlRestr, null));
//        OWLDataFactory df = owlRestr.getOWLDataFactory();
//        OWLDescription clone = null;
//        if (owlRestr.isAtLeast()) {
//            if (owlRestr.getAtLeast() == 1) { // transform into a some restriction
//                clone = df.getOWLObjectSomeRestriction(newProp, owlRestr.getDescription());
//            }
//            else {
//                clone = df.getOWLObjectCardinalityAtLeastRestriction(newProp, owlRestr.getAtLeast(),
//                                                                     owlRestr.getDescription());
//            }
//        }
//        else if (owlRestr.isAtMost()) {
//            if (owlRestr.getAtMost() == 0) { // transform into a negated some restriction
//                OWLObjectSomeRestriction someRestr = df.getOWLObjectSomeRestriction(newProp, owlRestr.getDescription());
//                clone = df.getOWLNot(someRestr);
//            }
//            else {
//                clone = df.getOWLObjectCardinalityAtMostRestriction(newProp, owlRestr.getAtMost(),
//                                                                    owlRestr.getDescription());
//            }
//        }
//        else if (owlRestr.isExactly()) {
//            clone = df.getOWLObjectCardinalityRestriction(newProp, owlRestr.getAtLeast(),
//                                                          owlRestr.getAtMost(), owlRestr.getDescription());
//        }
//        changes.add(new AddSuperClass(ont, cls, clone, null));
//    }
//
//    public List<OntologyChange> getChanges() {
//        return changes;
//    }
}
