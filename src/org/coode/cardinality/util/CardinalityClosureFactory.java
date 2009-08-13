package org.coode.cardinality.util;

import org.protege.editor.owl.model.util.ClosureAxiomFactory;
import org.semanticweb.owlapi.model.*;

import java.util.Set;

/**
 * Author: drummond<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 30, 2006<br><br>
 * <p/>
 * nick.drummond@cs.manchester.ac.uk<br>
 * www.cs.man.ac.uk/~drummond<br><br>
 * <p/>
 * Extended version of the ClosureAxiomFactory that also counts qualified cardinality
 * restrictions
 */
public class CardinalityClosureFactory extends ClosureAxiomFactory {

    public CardinalityClosureFactory(OWLObjectProperty objectProperty, OWLDataFactory owlDataFactory, Set<OWLOntology> ontologies) {
        super(objectProperty, owlDataFactory, ontologies);
    }

    public void visit(OWLObjectMinCardinality restr) {
        if (restr.getCardinality() > 0){
            accumulate(restr);
        }
    }

    public void visit(OWLObjectExactCardinality restr) {
        if (restr.getCardinality() > 0){
            accumulate(restr);
        }
    }

    private void accumulate(OWLObjectCardinalityRestriction restr){
        OWLClassExpression filler = restr.getFiller();
        if (!filler.equals(owlDataFactory.getOWLThing())) {
            fillers.add(filler);
        }
    }
}
