package org.coode.cardinality.util;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: drummond<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 25, 2006<br><br>
 * <p/>
 * nick.drummond@cs.manchester.ac.uk<br>
 * www.cs.man.ac.uk/~drummond<br><br>
 */
public class RestrictionUtils {

    public static final int INFINITE = -1;

    /**
     * @param owlRestr
     * @return the filler of the restriction (cardi will be qualified)
     */
    public static OWLObject getOWLFiller(OWLClassExpression owlRestr) {
        return new FillerFinder().getFiller(owlRestr);
    }

    /**
     * @param owlRestr
     * @return 0 if none set or value if otherwise
     */
    public static int getMinRelationships(OWLClassExpression owlRestr) {
        return new MinCardinalityFinder().getMin(owlRestr);
    }

    /**
     * @param owlRestr
     * @return -1 if infinite or value if otherwise
     */
    public static int getMaxRelationships(OWLClassExpression owlRestr) {
        return new MaxCardinalityFinder().getMax(owlRestr);
    }

    /**
     * Includes Complement classes that contain a restriction
     *
     * @param cls
     * @param mngr
     * @return set of conditions specified by <code>filterRestrictions</code> or empty list if none
     */
    public static Set<OWLClassExpression> getDirectRestrictionsOnClass(OWLClass cls, OWLModelManager mngr) {
        Set<OWLClassExpression> directRestrs = new HashSet<OWLClassExpression>();
        for (OWLOntology ont : mngr.getActiveOntologies()){
            directRestrs.addAll(cls.getSuperClasses(ont));
        }
        return filterRestrictions(directRestrs);
    }

    /**
     * Get only inherited restrictions for a given class
     * Includes Complement classes that contain a restriction
     *
     * @param cls
     * @param mngr
     * @return set of conditions specified by <code>filterRestrictions</code> or empty list if none
     */
    public static Set<OWLClassExpression> getInheritedRestrictionsOnClass(OWLClass cls, OWLModelManager mngr) {
        Set<OWLClassExpression> inheritedRestrs = new HashSet<OWLClassExpression>();
        Set<OWLClass> ancestors = mngr.getOWLHierarchyManager().getOWLClassHierarchyProvider().getAncestors(cls);
        for (OWLClass ancestor : ancestors) {
            for (OWLOntology ont : mngr.getActiveOntologies()){
                inheritedRestrs.addAll(ancestor.getSuperClasses(ont));
            }
        }
        return inheritedRestrs;
    }

    /**
     * @param all any number of OWLClassExpressions
     * @return a subset of all containing only restrictions and negated some restrictions or an empty list
     */
    public static Set<OWLClassExpression> filterRestrictions(Set<OWLClassExpression> all) {
        Set<OWLClassExpression> filtered = new HashSet<OWLClassExpression>();
        for (OWLClassExpression descr : all) {
            // @@TODO if we can make this read only, we should extract restrs from intersections (hard to maintain editing)
//            if (descr instanceof OWLObjectIntersectionOf){ // split the contents of intersections
//                filtered.addAll(filterRestrictions(((OWLObjectIntersectionOf)descr).getOperands()));
//            }
//            else
                if (descr instanceof OWLRestriction) {
                filtered.add(descr);
            }
            else if (isNotSome(descr)) {
                filtered.add(descr);
            }
        }
        return filtered;
    }

    public static boolean isSubPropOf(OWLObjectProperty prop, OWLObjectProperty onProp, OWLModelManager mngr) {
        OWLObjectHierarchyProvider<OWLObjectProperty> hp = mngr.getOWLHierarchyManager().getOWLObjectPropertyHierarchyProvider();
        return hp.getAncestors(prop).contains(onProp);
    }

    public static boolean isSubPropOf(OWLDataProperty prop, OWLDataProperty onProp, OWLModelManager mngr) {
        OWLObjectHierarchyProvider<OWLDataProperty> hp = mngr.getOWLHierarchyManager().getOWLDataPropertyHierarchyProvider();
        return hp.getAncestors(prop).contains(onProp);
    }

    public static Set<OWLObjectAllValuesFrom> getUniversals(OWLClass namedClass, OWLModelManager mngr) {
        Set<OWLObjectAllValuesFrom> candidates = new HashSet<OWLObjectAllValuesFrom>();
        Set<OWLClassExpression> restrs = getDirectRestrictionsOnClass(namedClass, mngr);
        restrs.addAll(getInheritedRestrictionsOnClass(namedClass, mngr));
        for (OWLClassExpression restr : restrs) {
            if (restr instanceof OWLObjectAllValuesFrom) {
                candidates.add((OWLObjectAllValuesFrom) restr);
            }
        }
        return candidates;
    }

    public static boolean isNotSome(OWLClassExpression descr) {
        return (descr instanceof OWLObjectComplementOf) &&
               (((OWLObjectComplementOf) descr).getOperand() instanceof OWLObjectSomeValuesFrom);
    }

    public static OWLProperty getProperty(OWLClassExpression restr) {
        if (restr instanceof OWLRestriction) {
            OWLPropertyExpression propExpression = ((OWLRestriction) restr).getProperty();
            if (propExpression instanceof OWLProperty){
                return (OWLProperty)propExpression;
            }
        }
        else if (isNotSome(restr)) {
            final OWLObjectSomeValuesFrom svf = ((OWLObjectSomeValuesFrom) ((OWLObjectComplementOf) restr).getOperand());
            OWLPropertyExpression propExpression = svf.getProperty();
            if (propExpression instanceof OWLProperty){
                return (OWLProperty)propExpression;
            }
        }
        return null;
    }

    //    /**
//     * Only returns fillers of Some and hasValue restrictions
//     * @param subject
//     * @param prop
//     * @param mngr
//     * @return
//     * @throws OWLException
//     */
//    public static Set<OWLClassExpression> getNamedFillers(OWLClass subject, OWLObjectProperty prop, OWLModelManager mngr) throws OWLException {
//        Set<OWLClassExpression> supers = subject.getSuperClasses(mngr.getActiveOntology());
//        ObjectSomeRestrictionFillerExtractor fillerCalc = new ObjectSomeRestrictionFillerExtractor(prop);
//        for (OWLClassExpression s : supers) {
//            s.accept(fillerCalc);
//        }
//
//        Set<OWLClassExpression> namedFillers = new HashSet<OWLClassExpression>();
//        for (OWLClassExpression filler : fillerCalc.getFillers()) {
//            if (filler instanceof OWLClass) {
//                namedFillers.add((OWLClassExpression) filler);
//            }
//        }
//        return namedFillers;
//    }

//    public static Set<OWLClass> filterClasses(Set<Set<OWLClass>> original) throws OWLException {
//        Set<OWLClass> result = new HashSet<OWLClass>();
//        for (Set<OWLClass> set : original) {
//            for (Iterator<OWLClass> it = set.iterator(); it.hasNext();) {
//                OWLClass subject = it.next();
//                if (subject.getURI().equals(OWLVocabularyAdapter.INSTANCE.getNothing())) {
//                    it.removeRows();
//                }
//                else {
//                    result.add(subject);
//                }
//            }
//        }
//        return result;
//    }
}
