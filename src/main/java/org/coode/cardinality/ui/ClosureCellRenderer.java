package org.coode.cardinality.ui;

import org.coode.cardinality.model.CardinalityRow;
import org.coode.cardinality.model.CardinalityTableModel;
import org.coode.cardinality.util.ClosureUtils;
import org.protege.editor.owl.model.OWLModelManager;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

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
 * nick.drummond@cs.manchester.ac.uk<br>
 * http://www.cs.man.ac.uk/~drummond<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Aug 30, 2006<br><br>
 * <p/>
 */
public class ClosureCellRenderer extends JCheckBox implements TableCellRenderer {

    private ClosureUtils closureUtil;

    public ClosureCellRenderer(OWLModelManager mngr) {
        super();
        closureUtil = new ClosureUtils(mngr);
        setHorizontalAlignment(CENTER);
        setOpaque(true);        
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean b, int row, int col) {
        boolean highlight = false;
        if (value.equals(Boolean.FALSE)) {
            CardinalityTableModel cardiModel = (CardinalityTableModel) table.getModel();
            CardinalityRow r = cardiModel.getRow(row);
            if (r.getMax() != 0) {
                // find if there is a candidate closure axiom for the property
                // (which means this filler must be missing)
                if (closureUtil.getCandidateClosureAxioms(r.getSubject(), r.getProperty()).size() > 0) {
                    highlight = true;
                }
            }
        }

        if (highlight){
            setBackground(Color.RED);
        }
        else{
            if (isSelected){
                setBackground(table.getSelectionBackground());
            }
            else{
                setBackground(table.getBackground());
            }
        }

        setSelected(((Boolean)value));
        
        return this;
    }
}
