package org.coode.cardinality.ui.celleditor;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Sep 7, 2007<br><br>
 */
public class OWLLiteralCellEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 3520275622320178920L;
	private OWLDatatype type;
    private OWLModelManager mngr;
    private String lang;

    public OWLLiteralCellEditor(JTextField jTextField, OWLModelManager mngr) {
        super(jTextField);
        this.mngr = mngr;
    }

    public Component getTableCellEditorComponent(JTable jTable, Object object, boolean b, int i, int i1) {
    	OWLLiteral literal = (OWLLiteral) object;
        // retain the existing type or language
        if (!literal.hasLang()){
            type = literal.getDatatype();
            lang = null;
        }
        else{
            type = null;
            lang = literal.getLang();
        }

        // unwrap the literal from the constant for editing
        object = literal.getLiteral();

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
