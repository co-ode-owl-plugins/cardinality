package org.coode.cardinality.util;

import java.util.Set;

import org.protege.editor.owl.model.util.ObjectSomeValuesFromFillerExtractor;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectCardinalityRestriction;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectProperty;

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
 * <p/>
 * Extended version of the ClosureAxiomFactory that also counts qualified
 * cardinality restrictions
 */
public class CardinalityClosureFactory
        extends ObjectSomeValuesFromFillerExtractor {

    protected OWLDataFactory df;

    public OWLObjectAllValuesFrom getClosureRestriction() {
        Set<OWLClassExpression> descriptions = getFillers();
        if (descriptions.isEmpty()) {
            return null;
        } else {
            if (descriptions.size() == 1) {
                return df.getOWLObjectAllValuesFrom(getObjectProperty(),
                        descriptions.iterator().next());
            } else {
                return df.getOWLObjectAllValuesFrom(getObjectProperty(),
                        df.getOWLObjectUnionOf(descriptions));
            }
        }
    }

    public CardinalityClosureFactory(OWLObjectProperty objectProperty,
            OWLDataFactory owlDataFactory) {
        super(owlDataFactory, objectProperty);
        df = owlDataFactory;
    }

    @Override
    public void visit(OWLObjectMinCardinality restr) {
        if (restr.getCardinality() > 0) {
            accumulate(restr);
        }
    }

    @Override
    public void visit(OWLObjectExactCardinality restr) {
        if (restr.getCardinality() > 0) {
            accumulate(restr);
        }
    }

    private void accumulate(OWLObjectCardinalityRestriction restr) {
        OWLClassExpression filler = restr.getFiller();
        if (!filler.isOWLThing()) {
            fillers.add(filler);
        }
    }
}
