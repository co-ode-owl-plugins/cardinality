package org.coode.cardinality.ui.roweditor;

import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLTypedLiteral;
import org.semanticweb.owlapi.vocab.OWLFacet;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
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
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Sep 7, 2007<br><br>
 */
public class DataRangeFacetPanel extends JPanel {

    private Map<OWLFacet, JTextField> componentMap = new HashMap<OWLFacet, JTextField>();
    private OWLEditorKit eKit;


    public DataRangeFacetPanel(OWLEditorKit eKit) {

        this.eKit = eKit;

        setLayout(new BorderLayout(4, 4));

        add(createToolbar(), BorderLayout.NORTH);
        add(createInternalPanel(), BorderLayout.CENTER);
    }

    private JComponent createToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setOpaque(false);
        toolBar.setFloatable(false);
        toolBar.setBorderPainted(false);
        toolBar.setBorder(null);
        toolBar.add(new AbstractAction("Reset Facets"){
            public void actionPerformed(ActionEvent actionEvent) {
                for (JTextField field : componentMap.values()){
                    field.setText("");
                }
            }
        });
        return toolBar;
    }

    private JComponent createInternalPanel() {
        JPanel internalPanel = new JPanel(new GridBagLayout());
        internalPanel.setBorder(BorderFactory.createEmptyBorder());
        internalPanel.setAlignmentY(0.0f);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = 0;
        gc.insets = new Insets(4, 4, 4, 4);

        for (IRI facetIRI : OWLFacet.getFacetIRIs()){
            OWLFacet facet = OWLFacet.getFacet(facetIRI);
            gc.gridx = 0;
            gc.anchor = GridBagConstraints.LINE_END;
            gc.fill = GridBagConstraints.NONE;
            gc.weightx = 0.1f;
            final JLabel label = new JLabel(facet.getSymbolicForm());
            label.setAlignmentX(1.0f);
            internalPanel.add(label, gc);

            JTextField editor = new JTextField();
            editor.setAlignmentX(0.0f);
            editor.setPreferredSize(new Dimension(100, editor.getPreferredSize().height));
            gc.gridx = 1;
            gc.anchor = GridBagConstraints.LINE_START;
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.weightx = 0.9f;
            internalPanel.add(editor, gc);
            componentMap.put(facet, editor);
            gc.gridy++;
        }
        return internalPanel;
    }

    public Map<OWLFacet, OWLTypedLiteral> getFacetValueMap(OWLDatatype baseType) {
        Map<OWLFacet, OWLTypedLiteral> results = new HashMap<OWLFacet, OWLTypedLiteral>();
        OWLDataFactory df = eKit.getModelManager().getOWLDataFactory();
        for (OWLFacet facet : componentMap.keySet()){
            final String valueStr = componentMap.get(facet).getText();
            if (valueStr != null && !valueStr.equals("")){
                OWLTypedLiteral value = df.getOWLTypedLiteral(valueStr, getOWLDataTypeForFacet(facet, baseType));
                results.put(facet, value);
            }
        }
        return results;
    }

    private OWLDatatype getOWLDataTypeForFacet(OWLFacet facet, OWLDatatype baseType) {
        OWLDataFactory df = eKit.getModelManager().getOWLDataFactory();
        switch(facet){
            case MIN_EXCLUSIVE:     // fallthrough
            case MIN_INCLUSIVE:     // fallthrough
            case MAX_EXCLUSIVE:     // fallthrough
            case MAX_INCLUSIVE:
                return baseType;
            case LENGTH:            // fallthrough
            case FRACTION_DIGITS:   // fallthrough
            case TOTAL_DIGITS:
                return df.getOWLDatatype(XSDVocabulary.INTEGER.getURI());
            default:
                return df.getOWLDatatype(XSDVocabulary.STRING.getURI());
        }
    }
}
