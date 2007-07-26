package org.coode.cardinality.ui;

import org.apache.log4j.Logger;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.description.OWLDescriptionParser;
import org.protege.editor.owl.ui.clsdescriptioneditor.ExpressionEditor;
import org.protege.editor.owl.ui.clsdescriptioneditor.OWLDescriptionChecker;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLException;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * Author: drummond<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 30, 2006<br><br>
 * <p/>
 * nick.drummond@cs.manchester.ac.uk<br>
 * www.cs.man.ac.uk/~drummond<br><br>
 */
public class OWLDescriptionCellEditor extends AbstractCellEditor implements TableCellEditor {

    private OWLModelManager owlModelManager;

    private ExpressionEditor owlDescriptionEditor;

    private boolean expandable;

    public static final int EXPANDABLE_ROW_HEIGHT = 45;

    public OWLDescriptionCellEditor(OWLEditorKit eKit) {
        this.owlModelManager = eKit.getOWLModelManager();
        owlDescriptionEditor = new ExpressionEditor<OWLDescription>(eKit, new OWLDescriptionChecker(eKit));
        owlDescriptionEditor.setOpaque(false);
        owlDescriptionEditor.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!e.isConsumed()) {
                        e.consume();
                        fireEditingStopped();
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (!e.isConsumed()) {
                        e.consume();
                        fireEditingCanceled();
                    }
                }
            }
        });
        expandable = false;
        owlDescriptionEditor.setFont(owlDescriptionEditor.getFont().deriveFont(Font.PLAIN, 12.0f));
    }

    /**
     * Makes the cell clsdescriptioneditor exandable.  If the cell clsdescriptioneditor is
     * expandable, then the the table
     * row that contains the cell being edited is expanded to give
     * the user a few lines to edit the expression.
     * @param expandable
     */
    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public void setEditorIndentation(int editorIndentation) {
        owlDescriptionEditor.setBorder(BorderFactory.createEmptyBorder(0, editorIndentation, 0, 0));
    }

    public Object getCellEditorValue() {
        OWLDescriptionParser parser = owlModelManager.getOWLDescriptionParser();
        OWLDescription descr = null;
        try {
            descr = parser.createOWLDescription(owlDescriptionEditor.getText());
        }
        catch (OWLException e) {
            Logger.getLogger(OWLDescriptionCellEditor.class).error(e.getMessage());
        }
        return descr;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (expandable) {
            table.setRowHeight(row, EXPANDABLE_ROW_HEIGHT);
        }

        OWLDescription desc = (OWLDescription) value;
        if (desc != null) {
            owlDescriptionEditor.setText(owlModelManager.getOWLObjectRenderer().render(desc, owlModelManager.getOWLEntityRenderer()));
        }
        else {
            owlDescriptionEditor.setText("");
        }
        return owlDescriptionEditor;
    }

    public boolean isCellEditable(EventObject eventObject) {
        if (eventObject instanceof MouseEvent) {
            return ((MouseEvent) eventObject).getClickCount() >= 2;
        }
        else {
            return super.isCellEditable(eventObject);
        }
    }
}
