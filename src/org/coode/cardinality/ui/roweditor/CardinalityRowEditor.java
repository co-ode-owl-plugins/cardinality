package org.coode.cardinality.ui.roweditor;

import org.coode.cardinality.model.CardinalityRow;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.model.OWLClass;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
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
public class CardinalityRowEditor extends JPanel {

    private JTabbedPane tabbedPane;

    private Map<String, CardinalityRowEditorPanel> editors = new HashMap<String, CardinalityRowEditorPanel>();

    private OWLEditorKit eKit;

    public CardinalityRowEditor(OWLEditorKit eKit, OWLClass subject) {
        setLayout(new BorderLayout());

        this.eKit = eKit;

        editors.put("Class", new ClassFillerRowEditor(eKit, subject));
        editors.put("Individual", new IndividualFillerRowEditor(eKit, subject));
        editors.put("Data Range", new DataRangeFillerRowEditor(eKit, subject));
        editors.put("Data Constant", new DataConstantFillerRowEditor(eKit, subject));

        tabbedPane = new JTabbedPane();
        tabbedPane.setFocusable(false);

        final java.util.List<String> editorNames = new ArrayList<String>(editors.keySet());
        Collections.sort(editorNames);
        for (String editorName : editorNames){
            tabbedPane.add(editorName, editors.get(editorName));
        }
        tabbedPane.setSelectedComponent(editors.get("Class"));
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    public int showDialog(OWLClass subject) {
        for (CardinalityRowEditorPanel editor : editors.values()){
            editor.setSubject(subject);
        }
        return JOptionPane.showConfirmDialog(eKit.getWorkspace().getTopLevelAncestor(),
                                             this, "Create New Row", JOptionPane.OK_CANCEL_OPTION,
                                             JOptionPane.PLAIN_MESSAGE);
    }

    public CardinalityRow createRow() {
        CardinalityRowEditorPanel activePanel = ((CardinalityRowEditorPanel)tabbedPane.getSelectedComponent());
        return activePanel.getRow();
    }

    public void dispose() {
        for (CardinalityRowEditorPanel editor : editors.values()){
            editor.dispose();
        }
    }
}
