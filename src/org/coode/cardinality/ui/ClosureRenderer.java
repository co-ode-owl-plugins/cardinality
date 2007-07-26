package org.coode.cardinality.ui;

import org.coode.cardinality.model.CardinalityRow;
import org.coode.cardinality.model.CardinalityTableModel;
import org.coode.cardinality.util.ClosureUtils;
import org.protege.editor.owl.model.OWLModelManager;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Author: drummond<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 25, 2006<br><br>
 * <p/>
 * nick.drummond@cs.manchester.ac.uk<br>
 * www.cs.man.ac.uk/~drummond<br><br>
 */
public class ClosureRenderer extends JCheckBox implements TableCellRenderer {

    private ClosureUtils closureUtil;

    public ClosureRenderer(OWLModelManager mngr) {
        super();
        closureUtil = new ClosureUtils(mngr);
        setHorizontalAlignment(CENTER);
        setBackground(Color.RED);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean b, int row, int col) {
        setHighlight(false);
        if (value.equals(Boolean.FALSE)) {
            CardinalityTableModel cardiModel = (CardinalityTableModel) table.getModel();
            CardinalityRow r = cardiModel.getRestriction(row);
            if (r.getMax() != 0) {
                // find if there is a candidate closure axiom for the property
                // (which means this filler must be missing)
                if (closureUtil.getCandidateClosureAxioms(r.getSubject(), r.getProperty()).size() > 0) {
                    setHighlight(true);
                }
            }
        }
        setSelected(Boolean.TRUE.equals(value));
        return this;
    }

    private void setHighlight(boolean highlight) {
        setOpaque(highlight);
    }
}
