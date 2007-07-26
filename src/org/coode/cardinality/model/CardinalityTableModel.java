package org.coode.cardinality.model;

import org.apache.log4j.Logger;
import org.coode.cardinality.prefs.CardinalityPreferences;
import org.coode.cardinality.prefs.CardinalityProperties;
import org.coode.cardinality.util.ClosureUtils;
import org.coode.cardinality.util.RestrictionUtils;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owl.model.*;

import javax.swing.table.AbstractTableModel;
import java.util.*;

/**
 * Author: drummond<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 25, 2006<br><br>
 * <p/>
 * nick.drummond@cs.manchester.ac.uk<br>
 * www.cs.man.ac.uk/~drummond<br><br>
 */
public class CardinalityTableModel extends AbstractTableModel {

    public static final int COL_PROP = 0;
    public static final int COL_MIN = 1;
    public static final int COL_MAX = 2;
    public static final int COL_FILLER = 3;
    public static final int COL_CLOSED = 4;
    public static final int COL_COUNT = 5;

    private static final Class[] COL_CLASSES = {OWLObjectProperty.class,
                                                Integer.class,
                                                Integer.class,
                                                OWLDescription.class,
                                                Boolean.class};

    private static String[] colNames;

    private OWLClass cls = null;

    private List<CardinalityRow> rows = new LinkedList<CardinalityRow>();

    private OWLModelManager mngr;

    private ClosureUtils closureUtils;

    private OWLOntologyChangeListener cl = new OWLOntologyChangeListener() {
        public void ontologiesChanged(List<? extends OWLOntologyChange> list) throws OWLException {
            boolean reload = false;
            for (OWLOntologyChange ontologyChange : list){
                OWLDescription subclass = null;
                OWLDescription superclass = null;
                OWLAxiom axiom = ontologyChange.getAxiom();
                if (axiom instanceof OWLSubClassAxiom) {
                    subclass = ((OWLSubClassAxiom) axiom).getSubClass();
                    superclass = ((OWLSubClassAxiom) axiom).getSuperClass();
                }

                if (subclass != null) {
                    if ((subclass == cls) ||
                        (mngr.getOWLClassHierarchyProvider().getAncestors(cls).contains(subclass))) {
                        if (superclass instanceof OWLClass ||
                            superclass instanceof OWLRestriction ||
                            superclass instanceof OWLObjectComplementOf) {
                            reload = true;
                        }
                    }
                }
            }
            if (reload){
                reload();
            }
        }
    };

    public CardinalityTableModel(OWLModelManager mngr) {
        super();
        this.mngr = mngr;
        closureUtils = new ClosureUtils(mngr);
        mngr.addOntologyChangeListener(cl);
    }

    public void setSubject(OWLClass newClass) {
        this.cls = newClass;
        reload();
    }

    public OWLClass getSubject() {
        return cls;
    }

    public void reload() {

        rows.clear();

        try {
            if (cls != null) {
                Set<OWLDescription> directSupers = RestrictionUtils.getDirectRestrictionsOnClass(cls, mngr);
                processOWLRestrictions(directSupers, false);

                if (showInherited()) {
                    Set<OWLDescription> inheritedRestrs = RestrictionUtils.getInheritedRestrictionsOnClass(cls, mngr);
                    processOWLRestrictions(inheritedRestrs, true);
                }

                Collections.sort(rows);
            }
        }
        catch (OWLException e) {
            Logger.getLogger(CardinalityTableModel.class).error(e);
        }
        finally {
            fireTableDataChanged();
        }
    }

    /**
     * Removal of the restrictions in a single row of the table
     * Warning - DO NOT USE THIS FOR MULTIPLE DELETES - as this will break undo
     *
     * @param row to delete
     */
    public void removeRestriction(CardinalityRow row) {
        List<OWLOntologyChange> changes = row.getDeleteChanges();
        if (row.getProperty() instanceof OWLObjectProperty && row.isClosed()) {
            changes.addAll(closureUtils.removeFromClosure(Collections.singleton((OWLDescription) row.getFiller()),
                                                          row.getSubject(), (OWLObjectProperty)row.getProperty()));
        }
        mngr.applyChanges(changes);
    }

    /**
     * Atomic removal of restrictions and management of closure (must be done in a single transaction for undo)
     *
     * @param rows the rows to delete
     */
    public void removeRestrictions(Collection<CardinalityRow> rows) {

        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();

        Map<OWLProperty, Set<OWLDescription>> closureMap = new HashMap<OWLProperty, Set<OWLDescription>>();

        for (CardinalityRow restr : rows) {
            if (!restr.isReadOnly()) {
                changes.addAll(restr.getDeleteChanges());

                // can't reset closure using the current state of the ontology (as nothing has been deleted yet)
                if (restr.isClosed()) {
                    // generate a map of properties to fillers that should be removed
                    Set<OWLDescription> fillers = closureMap.get(restr.getProperty());
                    if (fillers == null) {
                        fillers = new HashSet<OWLDescription>();
                        closureMap.put(restr.getProperty(), fillers);
                    }
                    fillers.add((OWLDescription) restr.getFiller());
                }
            }
        }

        for (OWLProperty prop : closureMap.keySet()) {
            if (prop instanceof OWLObjectProperty){
                changes.addAll(closureUtils.removeFromClosure(closureMap.get(prop),
                                                              getSubject(),
                                                              (OWLObjectProperty)prop));
            }
        }
        mngr.applyChanges(changes);
    }

    public CardinalityRow getRestriction(int row) {
        return rows.get(row);
    }

    public int getRow(CardinalityRow restr) {
        return rows.indexOf(restr);
    }

    public int getRow(OWLRestriction restr) {
        for (CardinalityRow row : rows) {
            if (row.contains(restr)) {
                return rows.indexOf(row);
            }
        }
        return -1;
    }

//////////////////////////////////////////////////////////

    private void processOWLRestrictions(Collection<OWLDescription> restrs, boolean readonly) throws OWLException {
        for (OWLDescription restr : restrs) { // any restriction
            CardinalityRow newRow = createRow(restr, readonly);
            if (newRow != null) {
                if (!mergeWithExistingRows(newRow)) {
                    rows.add(newRow);
                }
            }
        }
    }

    private CardinalityRow createRow(OWLDescription descr, boolean readonly) throws OWLException {
        CardinalityRow row = null;
        if ((RestrictionUtils.isNotSome(descr)) ||
            ((descr instanceof OWLRestriction) && !(descr instanceof OWLObjectAllRestriction))) {

            OWLProperty prop = RestrictionUtils.getProperty(descr);
            OWLObject filler = RestrictionUtils.getOWLFiller(descr);
            final boolean closed = closureUtils.isClosed(getSubject(), prop, filler, mngr);
            row = new CardinalityRowImpl(getSubject(),
                                         filler,
                                         prop,
                                         RestrictionUtils.getMinRelationships(descr),
                                         RestrictionUtils.getMaxRelationships(descr),
                                         closed,
                                         mngr);
            row.addRestriction(descr, readonly);
            row.setModel(this);
        }
        return row;
    }

    private boolean mergeWithExistingRows(CardinalityRow newRow) throws OWLException {
        boolean merged = false;
        for (Iterator<CardinalityRow> j = rows.iterator(); j.hasNext() && !merged;) {
            CardinalityRow cardiRow = j.next();
            if (canMerge(cardiRow, newRow)) {
                cardiRow.merge(newRow);
                merged = true;
            }
        }
        return merged;
    }

    // Simple implementation - does not deal with subsumption of properties and fillers
    protected boolean canMerge(CardinalityRow row1, CardinalityRow row2) {
        return (row1.getProperty().equals(row2.getProperty())) &&
               (row1.getFiller().equals(row2.getFiller()));
    }

//    /**
//     * If:
//     * - both the fillers are named classes
//     * - the restriction is not an OWLAllValuesFrom
//     * - the restricted property is an OWLObjectProperty
//     * - the restriction has more specific cardinality than that which already exists
//     */
//    protected boolean canMerge(CardinalityRow row1, CardinalityRow row2) {
//        boolean canMerge = false;
//            if ((row1.getFiller() instanceof OWLClass) &&
//                    (row2.getFiller() instanceof OWLClass)) {
//                    if (row2.getMax() >= row1.getMax()) {
//                        if (row2.getMax() <= row1.getMax()) {
//                            if (specialisesCurrentFiller((OWLClass) newFiller)) {
//                                // @@TODO test the property
//                                canMerge = true;
//                            }
//                        }
//                    }
//                }
//        return canMerge;
//    }
//
//    private boolean specialisesCurrentFiller(OWLClass newValue, OWLClass oldValue) {
//        return mngr.getOWLClassHierarchyProvider().getAncestors(newValue).contains(oldValue);
//    }

    private boolean showInherited() {
        return CardinalityPreferences.getInstance().getBoolean(CardinalityPreferences.OPT_SHOW_INHERITED_RESTRS, true);
    }

    protected void finalize() throws Throwable {
        mngr.removeOntologyChangeListener(cl);
        super.finalize();
    }

/////////////////////////////////// AbstractTableModel implementation

    public Class getColumnClass(int columnIndex) {
        return COL_CLASSES[columnIndex];
    }

    public String getColumnName(int column) {
        if (colNames == null) {
            Properties props = CardinalityProperties.getInstance();
            colNames = new String[COL_COUNT];
            for (int i = 0; i < COL_COUNT; i++) {
                colNames[i] = props.getProperty("col.name." + i, "unnamed");
            }
        }
        return colNames[column];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = null;
        CardinalityRow restr = rows.get(rowIndex);
        switch (columnIndex) {
//            case COL_AND:
//                value = (rowIndex > 0) ? "AND" : "";
//                break;
            case COL_MIN:
                int min = restr.getMin();
                value = (min < 0) ? "" : Integer.toString(min);
                break;
            case COL_MAX:
                int max = restr.getMax();
                value = (max < 0) ? "" : Integer.toString(max);
                break;
            case COL_PROP:
                value = restr.getProperty();
                break;
            case COL_FILLER:
                value = restr.getFiller();
                break;
            case COL_CLOSED:
                value = restr.isClosed();
                break;
        }

        return value;
    }

    public int getColumnCount() {
        return COL_COUNT;
    }

    public int getRowCount() {
        return rows.size();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean result = true;
        if (rows.get(rowIndex).isReadOnly()) {
            result = false;
        }
        else {
            switch (columnIndex) {
//                case COL_AND:
//                    result = false;
//                    break;
                case COL_FILLER: // drop through
                case COL_CLOSED:
                    OWLProperty prop = (OWLProperty) getValueAt(rowIndex, CardinalityTableModel.COL_PROP);
                    if (prop instanceof OWLDataProperty ||
                        getRestriction(rowIndex).getMax() == 0) {
                        result = false;
                    }
                    break;
            }
        }
        return result;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        CardinalityRow restr = rows.get(rowIndex);
        switch (columnIndex) {
            case COL_MIN:
                try {
                    restr.setMin(Integer.parseInt((String) aValue));
                }
                catch (NumberFormatException e) {
                    restr.setMin(CardinalityRow.NO_VALUE);
                }
                break;
            case COL_MAX:
                try {
                    restr.setMax(Integer.parseInt((String) aValue));
                }
                catch (NumberFormatException e) {
                    restr.setMax(CardinalityRow.NO_VALUE);
                }
                break;
            case COL_PROP:
                restr.setProperty((OWLObjectProperty) aValue);
                break;
            case COL_FILLER:
                restr.setFiller((OWLObject) aValue);
                break;
            case COL_CLOSED:
                restr.setClosed((Boolean) aValue);
                break;
        }

        changes.addAll(restr.getChanges());

        mngr.applyChanges(changes);

        fireTableDataChanged();
    }

    public Set<OWLDescription> getFillers(OWLObjectProperty property) {
        Set<OWLDescription> fillers = new HashSet<OWLDescription>();
        for (CardinalityRow row : rows) {
            if (row.getProperty().equals(property) &&
                row.getFiller() instanceof OWLDescription) {
                fillers.add((OWLDescription) row.getFiller());
            }
        }
        return fillers;
    }
}
