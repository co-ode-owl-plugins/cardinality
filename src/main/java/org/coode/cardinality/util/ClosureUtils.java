package org.coode.cardinality.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLRestriction;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.search.EntitySearcher;

/**
 * Author: drummond<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 30, 2006<br>
 * <br>
 * <p/>
 * nick.drummond@cs.manchester.ac.uk<br>
 * www.cs.man.ac.uk/~drummond<br>
 * <br>
 */
public class ClosureUtils {

    private final OWLModelManager mngr;

    public ClosureUtils(OWLModelManager mngr) {
        this.mngr = mngr;
    }

    /**
     * Use to find potential restrictions as a base for adding closure on a
     * particular property Will only return universals that have a named class
     * or union of named classes as filler
     *
     * @param namedClass
     *        the base class to be searched
     * @param prop
     *        will also search for universals on the superproperties of that
     *        given
     * @return all suitable restrictions for the given property or an empty list
     *         if none found
     */
    public Collection<OWLObjectAllValuesFrom>
            getCandidateClosureAxioms(OWLClass namedClass, OWLProperty prop) {
        Collection<OWLObjectAllValuesFrom> results = new ArrayList<>();
        if (prop instanceof OWLObjectProperty) {
            Set<OWLObjectAllValuesFrom> candidates = RestrictionUtils
                    .getUniversals(namedClass, mngr);
            for (OWLObjectAllValuesFrom restr : candidates) {
                OWLObjectPropertyExpression onProp = restr.getProperty();
                if (onProp instanceof OWLObjectProperty
                        && (onProp == prop || RestrictionUtils.isSubPropOf(
                                (OWLObjectProperty) prop,
                                (OWLObjectProperty) onProp, mngr))) {
                    OWLClassExpression filler = restr.getFiller();
                    if (filler instanceof OWLClass) {
                        results.add(restr);
                    } else if (filler instanceof OWLObjectUnionOf) {
                        addIfAllNamed(results, restr, filler);
                    }
                }
            }
        }
        return results;
    }

    protected void addIfAllNamed(Collection<OWLObjectAllValuesFrom> results,
            OWLObjectAllValuesFrom restr, OWLClassExpression filler) {
        for (OWLClassExpression next : ((OWLObjectUnionOf) filler)
                .getOperands()) {
            if (next.isAnonymous()) {
                return;
            }
        }
        results.add(restr);
    }

    /**
     * Use to find a closure axiom for a given property (that already contains
     * the given filler)
     *
     * @param namedClass
     *        the base class to be searched
     * @param prop
     *        will also search for universals on the superproperties of that
     *        given
     * @param filler
     *        will search the fillers to find one that contains this resource
     * @return the first universal restriction that matches both property
     */
    public OWLObjectAllValuesFrom getClosureAxiom(OWLClass namedClass,
            OWLObjectProperty prop, OWLObject filler) {
        Set<OWLObjectAllValuesFrom> candidates = RestrictionUtils
                .getUniversals(namedClass, mngr);
        for (OWLObjectAllValuesFrom restr : candidates) {
            OWLObjectPropertyExpression onProp = restr.getProperty();
            if (onProp instanceof OWLObjectProperty
                    && (onProp == prop || RestrictionUtils.isSubPropOf(prop,
                            (OWLObjectProperty) onProp, mngr))) {
                OWLClassExpression currentfiller = restr.getFiller();
                if (currentfiller instanceof OWLObjectUnionOf
                        && ((OWLObjectUnionOf) currentfiller).getOperands()
                                .contains(filler)) {
                    return restr;
                } else if (currentfiller.equals(filler)) {
                    return restr;
                }
            }
        }
        return null;
    }

    public List<OWLOntologyChange> resetClosure(boolean closed, OWLClass cls,
            OWLObjectProperty objProp) {
        List<OWLOntologyChange> changes = new ArrayList<>();
        Set<OWLAxiom> oldAxioms = new HashSet<>();
        OWLDataFactory df = mngr.getOWLDataFactory();
        OWLAxiom newAxiom = null;
        if (closed) {
            newAxiom = df.getOWLSubClassOfAxiom(cls,
                    createClosureAxiom(objProp, cls));
        }
        // get all existing closure axioms along this property
        for (OWLObjectAllValuesFrom candidate : getCandidateClosureAxioms(cls,
                objProp)) {
            oldAxioms.add(df.getOWLSubClassOfAxiom(cls, candidate));
        }
        if (oldAxioms.isEmpty()) {
            if (closed) {
                changes.add(new AddAxiom(mngr.getActiveOntology(), newAxiom));
            }
        } else {
            for (OWLAxiom oldAxiom : oldAxioms) {
                for (OWLOntology ont : mngr.getActiveOntologies()) {
                    if (ont.containsAxiom(oldAxiom)) {
                        changes.add(new RemoveAxiom(ont, oldAxiom));
                        if (closed) {
                            changes.add(new AddAxiom(ont, newAxiom));
                        }
                    }
                }
            }
        }
        return changes;
    }

    public OWLObjectAllValuesFrom createClosureAxiom(OWLObjectProperty objProp,
            OWLClass cls) {
        CardinalityClosureFactory closureAxiomFactory = new CardinalityClosureFactory(
                objProp, mngr.getOWLDataFactory());
        for (OWLClassExpression desc : getSuperclasses(cls)) {
            desc.accept(closureAxiomFactory);
        }
        for (OWLClassExpression desc : getEquivalentClasses(cls)) {
            desc.accept(closureAxiomFactory);
        }
        return closureAxiomFactory.getClosureRestriction();
    }

    public OWLObjectAllValuesFrom createClosureAxiom(OWLObjectProperty objProp,
            Set<OWLClassExpression> fillers) {
        OWLClassExpression filler;
        if (fillers.size() > 1) {
            filler = mngr.getOWLDataFactory().getOWLObjectUnionOf(fillers);
        } else {
            filler = fillers.iterator().next();
        }
        return mngr.getOWLDataFactory().getOWLObjectAllValuesFrom(objProp,
                filler);
    }

    public boolean isClosed(OWLClass subject, OWLProperty property,
            OWLObject filler) {
        if (property instanceof OWLObjectProperty) {
            return getClosureAxiom(subject, (OWLObjectProperty) property,
                    filler) != null;
        }
        return false;
    }

    /**
     * Cannot use resetClosure for this in the cases where we have not yet
     * removed the matching existentials
     * 
     * @param fillers fillers
     * @param cls class
     * @param property property
     * @return changes to apply
     */
    public List<OWLOntologyChange> removeFromClosure(
            Set<OWLClassExpression> fillers, OWLClass cls,
            OWLObjectProperty property) {
        List<OWLOntologyChange> changes = new ArrayList<>();
        OWLDataFactory df = mngr.getOWLDataFactory();
        for (OWLObjectAllValuesFrom candidate : getCandidateClosureAxioms(cls,
                property)) {
            OWLSubClassOfAxiom oldAxiom = df.getOWLSubClassOfAxiom(cls,
                    candidate);
            OWLSubClassOfAxiom newAxiom = null;
            OWLClassExpression descr = candidate.getFiller();
            if (descr instanceof OWLObjectUnionOf) { // regenerate the closure
                                                     // without these fillers
                Set<OWLClassExpression> operands = new HashSet<>(
                        ((OWLObjectUnionOf) descr).getOperands());
                operands.removeAll(fillers);
                if (operands.size() == 1) {
                    OWLRestriction restr = df.getOWLObjectAllValuesFrom(
                            property, operands.iterator().next());
                    newAxiom = df.getOWLSubClassOfAxiom(cls, restr);
                } else if (operands.size() > 1) {
                    OWLClassExpression newUnion = df
                            .getOWLObjectUnionOf(operands);
                    OWLRestriction restr = df
                            .getOWLObjectAllValuesFrom(property, newUnion);
                    newAxiom = df.getOWLSubClassOfAxiom(cls, restr);
                }
            }
            for (OWLOntology ont : mngr.getActiveOntologies()) {
                if (ont.containsAxiom(oldAxiom)) {
                    changes.add(new RemoveAxiom(ont, oldAxiom));
                    if (newAxiom != null) {
                        changes.add(new AddAxiom(ont, newAxiom));
                    }
                }
            }
        }
        return changes;
    }

    private Set<OWLClassExpression> getSuperclasses(OWLClass cls) {
        final Set<OWLClassExpression> superclasses = new HashSet<>();
        for (OWLOntology ont : mngr.getActiveOntologies()) {
            superclasses.addAll(EntitySearcher.getSuperClasses(cls, ont));
        }
        return superclasses;
    }

    private Collection<OWLClassExpression> getEquivalentClasses(OWLClass cls) {
        return EntitySearcher.getEquivalentClasses(cls,
                mngr.getActiveOntologies());
    }
}
