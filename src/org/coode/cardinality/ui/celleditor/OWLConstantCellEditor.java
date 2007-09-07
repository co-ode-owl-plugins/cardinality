package org.coode.cardinality.ui.celleditor;

import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.model.OWLTypedConstant;
import org.semanticweb.owl.model.OWLUntypedConstant;

import javax.swing.*;
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
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Sep 7, 2007<br><br>
 */
public class OWLConstantCellEditor extends DefaultCellEditor {

    private OWLDataType type;
    private OWLModelManager mngr;
    private String lang;

    public OWLConstantCellEditor(JTextField jTextField, OWLModelManager mngr) {
        super(jTextField);
        this.mngr = mngr;
    }

    public Component getTableCellEditorComponent(JTable jTable, Object object, boolean b, int i, int i1) {

        // retain the existing type or language
        if (object instanceof OWLTypedConstant){
            type = ((OWLTypedConstant)object).getDataType();
            lang = null;
        }
        else{
            type = null;
            lang = ((OWLUntypedConstant)object).getLang();
        }

        // unwrap the literal from the constant for editing
        object = ((OWLConstant)object).getLiteral();

        return super.getTableCellEditorComponent(jTable, object, b, i, i1);
    }

    public Object getCellEditorValue() {
        Object value = super.getCellEditorValue();
        if (type != null){
            value = mngr.getOWLDataFactory().getOWLTypedConstant((String)value, type);
        }
        else{
            if (lang != null){
                value = mngr.getOWLDataFactory().getOWLUntypedConstant((String)value, lang);
            }
            else{
                value = mngr.getOWLDataFactory().getOWLUntypedConstant((String)value);
            }
        }
        return value;
    }
}
