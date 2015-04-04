package org.coode.cardinality.util;

import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
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
 * http://www.cs.man.ac.uk/~drummond/<br>
 * <br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Jul 25, 2007<br>
 * <br>
 */
public class MaxCardinalityFinder extends OWLObjectVisitorAdapter {

    public static final int INFINITE = -1;
    private int max;

    public int getMax(OWLObject obj) {
        max = INFINITE;
        obj.accept(this);
        return max;
    }

    @Override
    public void visit(OWLObjectMaxCardinality restr) {
        max = restr.getCardinality();
    }

    @Override
    public void visit(OWLObjectExactCardinality restr) {
        max = restr.getCardinality();
    }

    @Override
    public void visit(OWLObjectHasValue restr) {
        max = 1;
    }

    @Override
    public void visit(OWLDataMaxCardinality restr) {
        max = restr.getCardinality();
    }

    @Override
    public void visit(OWLDataExactCardinality restr) {
        max = restr.getCardinality();
    }

    @Override
    public void visit(OWLDataHasValue owlDataValueRestriction) {
        max = 1;
    }

    @Override
    public void visit(OWLObjectComplementOf owlObjectComplementOf) {
        if (owlObjectComplementOf
                .getOperand() instanceof OWLObjectSomeValuesFrom) {
            max = 0;
        }
        // @@TODO what about other complements? (eg of cardinality restrictions)
    }
}
