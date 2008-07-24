package org.coode.cardinality.ui.celleditor;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.renderer.OWLCellRenderer;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.vocab.XSDVocabulary;

import javax.swing.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
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
 * Date: Sep 6, 2007<br><br>
 */
public class DataTypeCombo extends JComboBox {

    private OWLModelManager mngr;

    private boolean changesMade = false;

    public DataTypeCombo(OWLEditorKit eKit) {
        super();

        mngr = eKit.getModelManager();

        setRenderer(new OWLCellRenderer(eKit));

        load();
    }

    public void setPopupVisible(boolean v) {
        if (changesMade) {
            reload();
            changesMade = false;
        }
        super.setPopupVisible(v);
    }

    private void reload() {
        removeAllItems();
        load();
    }

    private void load() {
        List<OWLDataType> dts = new ArrayList<OWLDataType>();
        for (URI uri : XSDVocabulary.ALL_DATATYPES){
            dts.add(mngr.getOWLDataFactory().getOWLDataType(uri));
        }
        addItems(dts);
    }

    private void addItems(List<? extends OWLDataType> dataTypes) {
        if (dataTypes != null) {
            for (OWLDataType prop : dataTypes) {
                addItem(prop);
            }
        }
    }
}
