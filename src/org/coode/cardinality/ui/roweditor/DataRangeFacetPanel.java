package org.coode.cardinality.ui.roweditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWLFacet;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

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

    public Map<OWLFacet, OWLLiteral> getFacetValueMap(OWLDatatype baseType) {
        Map<OWLFacet, OWLLiteral> results = new HashMap<OWLFacet, OWLLiteral>();
        OWLDataFactory df = eKit.getModelManager().getOWLDataFactory();
        for (OWLFacet facet : componentMap.keySet()){
            final String valueStr = componentMap.get(facet).getText();
            if (valueStr != null && !valueStr.equals("")){
            	OWLLiteral value = df.getOWLLiteral(valueStr, getOWLDataTypeForFacet(facet, baseType));
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
                return df.getOWLDatatype(XSDVocabulary.INTEGER.getIRI());
            default:
                return df.getOWLDatatype(XSDVocabulary.STRING.getIRI());
        }
    }
}
