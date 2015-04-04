package org.coode.cardinality.util;

import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
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
import org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br>
 * <br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Sep 4, 2007<br>
 * <br>
 */
public class FillerFinder extends OWLObjectVisitorExAdapter<OWLObject> {

    /**
     * Default constructor.
     */
    public FillerFinder() {
        super(null);
    }

    @Override
    public OWLObject visit(OWLObjectSomeValuesFrom desc) {
        return desc.getFiller();
    }

    @Override
    public OWLObject visit(OWLObjectAllValuesFrom desc) {
        return desc.getFiller();
    }

    @Override
    public OWLObject visit(OWLObjectHasValue desc) {
        return desc.getFiller();
    }

    @Override
    public OWLObject visit(OWLObjectMinCardinality desc) {
        return desc.getFiller();
    }

    @Override
    public OWLObject visit(OWLObjectExactCardinality desc) {
        return desc.getFiller();
    }

    @Override
    public OWLObject visit(OWLObjectMaxCardinality desc) {
        return desc.getFiller();
    }

    @Override
    public OWLObject visit(OWLDataSomeValuesFrom desc) {
        return desc.getFiller();
    }

    @Override
    public OWLObject visit(OWLDataAllValuesFrom desc) {
        return desc.getFiller();
    }

    @Override
    public OWLObject visit(OWLDataHasValue desc) {
        return desc.getFiller();
    }

    @Override
    public OWLObject visit(OWLDataMinCardinality desc) {
        return desc.getFiller();
    }

    @Override
    public OWLObject visit(OWLDataExactCardinality desc) {
        return desc.getFiller();
    }

    @Override
    public OWLObject visit(OWLDataMaxCardinality desc) {
        return desc.getFiller();
    }

    @Override
    public OWLObject visit(OWLObjectComplementOf desc) {
        return desc.getOperand().accept(this);
    }
}
