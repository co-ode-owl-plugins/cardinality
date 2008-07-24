package org.coode.cardinality.ui.roweditor;

import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.list.OWLObjectList;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.vocab.XSDVocabulary;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
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
public class MyDataRangeSelectorPanel extends JPanel {

    private OWLEditorKit owlEditorKit;
    private OWLObjectList list;

    public MyDataRangeSelectorPanel(OWLEditorKit owlEditorKit) {
        this.owlEditorKit = owlEditorKit;
        createUI();
    }

    private void createUI() {
        setLayout(new BorderLayout(20, 20));

        final JToolBar toolBar = new JToolBar();
        toolBar.setOpaque(false);
        toolBar.setFloatable(false);
        toolBar.setBorderPainted(false);
        toolBar.setBorder(null);        
        add(toolBar, BorderLayout.NORTH);

        list = new OWLObjectList(owlEditorKit);

        // Add the built in datatypes
        OWLDataFactory df = owlEditorKit.getModelManager().getOWLDataFactory();
        java.util.List<OWLDataType> builtInDataTypes = new ArrayList<OWLDataType>();
        for (URI uri : XSDVocabulary.ALL_DATATYPES) {
            builtInDataTypes.add(df.getOWLDataType(uri));
        }
        Collections.sort(builtInDataTypes, owlEditorKit.getModelManager().getOWLObjectComparator());
        list.setListData(builtInDataTypes.toArray());
        list.setSelectedIndex(0);

        add(ComponentFactory.createScrollPane(list), BorderLayout.CENTER);
    }

    public OWLDataType getSelectedDataType(){
        return (OWLDataType)list.getSelectedValue();
    }
}
