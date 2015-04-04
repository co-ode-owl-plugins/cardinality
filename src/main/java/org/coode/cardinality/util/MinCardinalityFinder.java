package org.coode.cardinality.util;

import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
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
public class MinCardinalityFinder extends OWLObjectVisitorAdapter {

    public static final int INFINITE = -1;
    private int min;

    public int getMin(OWLObject obj) {
        min = INFINITE;
        obj.accept(this);
        return min;
    }

    @Override
    public void visit(OWLObjectSomeValuesFrom owlObjectSomeRestriction) {
        min = 1;
    }

    @Override
    public void visit(OWLObjectHasValue owlObjectValueRestriction) {
        min = 1;
    }

    @Override
    public void visit(OWLObjectMinCardinality cardi) {
        min = cardi.getCardinality();
    }

    @Override
    public void visit(OWLObjectExactCardinality cardi) {
        min = cardi.getCardinality();
    }

    @Override
    public void visit(OWLDataSomeValuesFrom owlDataSomeRestriction) {
        min = 1;
    }

    @Override
    public void visit(OWLDataHasValue owlDataValueRestriction) {
        min = 1;
    }

    @Override
    public void visit(OWLDataMinCardinality cardi) {
        min = cardi.getCardinality();
    }

    @Override
    public void visit(OWLDataExactCardinality cardi) {
        min = cardi.getCardinality();
    }
}
