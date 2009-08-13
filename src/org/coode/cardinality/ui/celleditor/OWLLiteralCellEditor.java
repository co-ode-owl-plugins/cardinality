package org.coode.cardinality.ui.celleditor;

import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLStringLiteral;
import org.semanticweb.owlapi.model.OWLTypedLiteral;

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
public class OWLLiteralCellEditor extends DefaultCellEditor {

    private OWLDatatype type;
    private OWLModelManager mngr;
    private String lang;

    public OWLLiteralCellEditor(JTextField jTextField, OWLModelManager mngr) {
        super(jTextField);
        this.mngr = mngr;
    }

    public Component getTableCellEditorComponent(JTable jTable, Object object, boolean b, int i, int i1) {

        // retain the existing type or language
        if (object instanceof OWLTypedLiteral){
            type = ((OWLTypedLiteral)object).getDatatype();
            lang = null;
        }
        else{
            type = null;
            lang = ((OWLStringLiteral)object).getLang();
        }

        // unwrap the literal from the constant for editing
        object = ((OWLLiteral)object).getLiteral();

        return super.getTableCellEditorComponent(jTable, object, b, i, i1);
    }

    public Object getCellEditorValue() {
        Object value = super.getCellEditorValue();
        if (value != null){
            if (type != null){
                value = mngr.getOWLDataFactory().getOWLTypedLiteral((String)value, type);
            }
            else{
                if (lang != null){
                    value = mngr.getOWLDataFactory().getOWLStringLiteral((String)value, lang);
                }
                else{
                    value = mngr.getOWLDataFactory().getOWLStringLiteral((String)value);
                }
            }
        }
        return value;
    }
}
