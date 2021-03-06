package org.coode.cardinality.ui.roweditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;

import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.selector.OWLDataPropertySelectorPanel;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.vocab.OWLFacet;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br>
 * <br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Sep 7, 2007<br>
 * <br>
 */
public class DataRangeFillerRowEditor extends CardinalityRowEditorPanel {
    private static final long serialVersionUID = 1L;

    private OWLDataPropertySelectorPanel dataPropertySelectorPanel;
    private MyDataRangeSelectorPanel rangeSelectorPanel;
    private final DataRangeFacetPanel facetEditorPanel;

    public DataRangeFillerRowEditor(OWLEditorKit eKit, OWLClass subject) {
        super(eKit, subject);
        dataPropertySelectorPanel = new OWLDataPropertySelectorPanel(eKit);
        dataPropertySelectorPanel.setBorder(
                ComponentFactory.createTitledBorder("Restricted properties"));
        rangeSelectorPanel = new MyDataRangeSelectorPanel(eKit);
        rangeSelectorPanel
                .setBorder(ComponentFactory.createTitledBorder("Data Range"));
        rangeSelectorPanel.setPreferredSize(new Dimension(200,
                rangeSelectorPanel.getPreferredSize().height));
        facetEditorPanel = new DataRangeFacetPanel(eKit);
        facetEditorPanel
                .setBorder(ComponentFactory.createTitledBorder("Facets"));
        facetEditorPanel.setPreferredSize(
                new Dimension(200, facetEditorPanel.getPreferredSize().height));
        JSplitPane rangeAndFacetSplitter = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, false);
        rangeAndFacetSplitter.setResizeWeight(0.5);
        rangeAndFacetSplitter.setLeftComponent(rangeSelectorPanel);
        rangeAndFacetSplitter.setRightComponent(facetEditorPanel);
        rangeAndFacetSplitter.setBorder(BorderFactory.createEmptyBorder());
        setLayout(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                false);
        splitPane.setResizeWeight(0.3);
        splitPane.setLeftComponent(dataPropertySelectorPanel);
        splitPane.setRightComponent(rangeAndFacetSplitter);
        add(splitPane, BorderLayout.CENTER);
        splitPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        add(createCardinalityPanel(), BorderLayout.SOUTH);
    }

    @Override
    protected OWLObject getSelectedFiller() {
        OWLDatatype baseType = rangeSelectorPanel.getSelectedDataType();
        Map<OWLFacet, OWLLiteral> facetMap = facetEditorPanel
                .getFacetValueMap(baseType);
        OWLDataFactory df = getOWLEditorKit().getModelManager()
                .getOWLDataFactory();
        Set<OWLFacetRestriction> facetRestrs = new HashSet<>();
        for (OWLFacet facet : facetMap.keySet()) {
            facetRestrs
                    .add(df.getOWLFacetRestriction(facet, facetMap.get(facet)));
        }
        if (facetRestrs.isEmpty()) {
            return baseType;
        } else {
            return df.getOWLDatatypeRestriction(baseType, facetRestrs);
        }
    }

    @Override
    protected OWLProperty getSelectedProperty() {
        return dataPropertySelectorPanel.getSelectedObject();
    }

    @Override
    public void dispose() {
        dataPropertySelectorPanel.dispose();
        dataPropertySelectorPanel = null;
        rangeSelectorPanel = null;
    }
}
