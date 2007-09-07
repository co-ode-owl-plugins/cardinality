package org.coode.cardinality.util;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.semanticweb.owl.model.*;

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
    public static OWLObject getOWLFiller(OWLDescription owlRestr) {
        return new FillerFinder().getFiller(owlRestr);
    }

    /**
     * @param owlRestr
     * @return 0 if none set or value if otherwise
     */
    public static int getMinRelationships(OWLDescription owlRestr) {
        return new MinCardinalityFinder().getMin(owlRestr);
    }

    /**
     * @param owlRestr
     * @return -1 if infinite or value if otherwise
     */
    public static int getMaxRelationships(OWLDescription owlRestr) {
        return new MaxCardinalityFinder().getMax(owlRestr);
    }

    /**
     * Includes Complement classes that contain a restriction
     *
     * @param cls
     * @param mngr
     * @return set of conditions specified by <code>filterRestrictions</code> or empty list if none
     */
    public static Set<OWLDescription> getDirectRestrictionsOnClass(OWLClass cls, OWLModelManager mngr) {
        Set<OWLDescription> directRestrs = new HashSet<OWLDescription>();
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
    public static Set<OWLDescription> getInheritedRestrictionsOnClass(OWLClass cls, OWLModelManager mngr) {
        Set<OWLDescription> inheritedRestrs = new HashSet<OWLDescription>();
        Set<OWLClass> ancestors = mngr.getOWLClassHierarchyProvider().getAncestors(cls);
        for (OWLClass ancestor : ancestors) {
            for (OWLOntology ont : mngr.getActiveOntologies()){
                inheritedRestrs.addAll(ancestor.getSuperClasses(ont));
            }
        }
        return inheritedRestrs;
    }

    /**
     * @param all any number of OWLDescriptions
     * @return a subset of all containing only restrictions and negated some restrictions or an empty list
     */
    public static Set<OWLDescription> filterRestrictions(Set<OWLDescription> all) {
        Set<OWLDescription> filtered = new HashSet<OWLDescription>();
        for (OWLDescription descr : all) {
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
        OWLObjectHierarchyProvider<OWLObjectProperty> hp = mngr.getOWLObjectPropertyHierarchyProvider();
        return hp.getAncestors(prop).contains(onProp);
    }

    public static boolean isSubPropOf(OWLDataProperty prop, OWLDataProperty onProp, OWLModelManager mngr) {
        OWLObjectHierarchyProvider<OWLDataProperty> hp = mngr.getOWLDataPropertyHierarchyProvider();
        return hp.getAncestors(prop).contains(onProp);
    }

    public static Set<OWLObjectAllRestriction> getUniversals(OWLClass namedClass, OWLModelManager mngr) {
        Set<OWLObjectAllRestriction> candidates = new HashSet<OWLObjectAllRestriction>();
        Set<OWLDescription> restrs = getDirectRestrictionsOnClass(namedClass, mngr);
        restrs.addAll(getInheritedRestrictionsOnClass(namedClass, mngr));
        for (OWLDescription restr : restrs) {
            if (restr instanceof OWLObjectAllRestriction) {
                candidates.add((OWLObjectAllRestriction) restr);
            }
        }
        return candidates;
    }

    public static boolean isNotSome(OWLDescription descr) {
        return (descr instanceof OWLObjectComplementOf) &&
               (((OWLObjectComplementOf) descr).getOperand() instanceof OWLObjectSomeRestriction);
    }

    public static OWLProperty getProperty(OWLDescription restr) {
        if (restr instanceof OWLRestriction) {
            OWLPropertyExpression propExpression = ((OWLRestriction) restr).getProperty();
            if (propExpression instanceof OWLProperty){
                return (OWLProperty)propExpression;
            }
        }
        else if (isNotSome(restr)) {
            final OWLObjectSomeRestriction svf = ((OWLObjectSomeRestriction) ((OWLObjectComplementOf) restr).getOperand());
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
//    public static Set<OWLDescription> getNamedFillers(OWLClass subject, OWLObjectProperty prop, OWLModelManager mngr) throws OWLException {
//        Set<OWLDescription> supers = subject.getSuperClasses(mngr.getActiveOntology());
//        ObjectSomeRestrictionFillerExtractor fillerCalc = new ObjectSomeRestrictionFillerExtractor(prop);
//        for (OWLDescription s : supers) {
//            s.accept(fillerCalc);
//        }
//
//        Set<OWLDescription> namedFillers = new HashSet<OWLDescription>();
//        for (OWLDescription filler : fillerCalc.getFillers()) {
//            if (filler instanceof OWLClass) {
//                namedFillers.add((OWLDescription) filler);
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
