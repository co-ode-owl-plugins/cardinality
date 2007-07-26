package org.coode.cardinality.util;

import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.OWLObjectVisitorAdapter;
import org.protege.editor.owl.model.OWLModelManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: drummond<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 29, 2006<br><br>
 * <p/>
 * nick.drummond@cs.manchester.ac.uk<br>
 * www.cs.man.ac.uk/~drummond<br><br>
 */
public class RestrictionFillerSetter extends OWLObjectVisitorAdapter {

//    private OWLModelManager mngr;
//
//    private OWLClass cls;
//    private OWLObject newFiller;
//
//    List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
//
//    public RestrictionFillerSetter(OWLOntology ont, OWLClass cls, OWLObject newFiller, OWLModelManager mngr) {
//        this.cls = cls;
//        this.newFiller = newFiller;
//        this.mngr = mngr;
//    }
//
//    public List<OWLOntologyChange> setFiller (OWLRestriction restr, OWLObject newFiller){
//        changes.clear();
//
//    }
//
//    public void visit(OWLObjectSomeRestriction owlRestr) throws OWLException {
//        changes.add(new RemoveSuperClass(ont, cls, owlRestr, null));
//        OWLObjectSomeRestriction clone = df.getOWLObjectSomeRestriction(owlRestr.getObjectProperty(),
//                                                                        (OWLDescription) newFiller);
//        changes.add(new AddSuperClass(ont, cls, clone, null));
//    }
//
//    public void visit(OWLObjectAllRestriction owlRestr) throws OWLException {
//        changes.add(new RemoveSuperClass(ont, cls, owlRestr, null));
//        OWLObjectAllRestriction clone = df.getOWLObjectAllRestriction(owlRestr.getObjectProperty(),
//                                                                      (OWLDescription) newFiller);
//        changes.add(new AddSuperClass(ont, cls, clone, null));
//    }
//
//    public void visit(OWLObjectValueRestriction owlRestr) throws OWLException {
//        changes.add(new RemoveSuperClass(ont, cls, owlRestr, null));
//        OWLObjectValueRestriction clone = df.getOWLObjectValueRestriction(owlRestr.getObjectProperty(),
//                                                                          (OWLIndividual) newFiller);
//        changes.add(new AddSuperClass(ont, cls, clone, null));
//    }
//
//    public void visit(OWLObjectCardinalityRestriction owlRestr) throws OWLException {
//        OWLObjectCardinalityRestriction clone = null;
//        if (owlRestr.isAtLeast()) {
//        }
//        else if (owlRestr.isAtMost()) {
//            clone = df.getOWLObjectCardinalityAtMostRestriction(owlRestr.getObjectProperty(),
//                                                                owlRestr.getAtMost(),
//                                                                owlRestr.getDescription());
//        }
//        else if (owlRestr.isExactly()) {
//            clone = df.getOWLObjectCardinalityRestriction(owlRestr.getObjectProperty(),
//                                                          owlRestr.getAtLeast(), owlRestr.getAtMost(),
//                                                          owlRestr.getDescription());
//        }
//        changes.add(new AddSuperClass(ont, cls, clone, null));
//    }
//
//
//    public void visit(OWLObjectMinCardinalityRestriction owlRestr) {
//        OWLAxiom oldAxiom = df.getOWLSubClassAxiom(cls, owlRestr);
//        for (OWLOntology ont : mngr.getA)
//        changes.add(new RemoveSuperClass(ont, cls, owlRestr, null));
//
//        OWLObjectCardinalityRestriction clone = df.getOWLObjectMinCardinalityRestriction(owlRestr.getProperty(),
//                                                             owlRestr.getCardinality(),
//                                                             owlRestr.getFiller());
//        changes.add(new AddSuperClass(ont, cls, clone, null));
//    }
//
//    public void visit(OWLObjectComplementOf owlRestr) throws OWLException {
//        if (RestrictionUtils.isNotSome(owlRestr)) {
//            changes.add(new RemoveSuperClass(ont, cls, owlRestr, null));
//            OWLObjectSomeRestriction some = (OWLObjectSomeRestriction) owlRestr.getOperand();
//            OWLObjectSomeRestriction someClone = df.getOWLObjectSomeRestriction(some.getObjectProperty(),
//                                                                                (OWLDescription) newFiller);
//            OWLObjectComplementOf clone = df.getOWLObjectComplementOf(someClone);
//            changes.add(new AddSuperClass(ont, cls, clone, null));
//        }
//    }
//
//    public List<OWLOntologyChange> getChanges() {
//        return changes;
//    }
}