package org.coode.cardinality.prefs;

import org.coode.cardinality.ui.CardinalityView;
import org.protege.editor.core.prefs.Preferences;
import org.protege.editor.core.prefs.PreferencesManager;

/**
 * Author: drummond<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 25, 2006<br><br>
 * <p/>
 * nick.drummond@cs.manchester.ac.uk<br>
 * www.cs.man.ac.uk/~drummond<br><br>
 */
public class CardinalityPreferences {

    private static CardinalityPreferences instance;

    private Preferences prefs;

    public static final String OPT_COMPLEX_FILLERS_ALLOWED = "opt_complex_fillers_allowed";
    public static final String OPT_SHOW_INHERITED_RESTRS = "opt_show_inherited_restrs";
    public static final String OPT_DOUBLE_CLICK_NAV = "opt_double_click_nav";
    public static final String OPT_EDIT_DT_PROPERTIES = "opt_edit_dt_properties";
    public static final String OPT_CREATE_PROPERTIES_INLINE = "opt_create_properties_inline";


    public static Preferences getInstance() {
        if (instance == null) {
            instance = new CardinalityPreferences();
        }
        return instance.prefs;
    }

    private CardinalityPreferences() {
        prefs = PreferencesManager.getInstance().getPreferencesForSet("org.coode.cardinality",
                                                                      CardinalityView.class);
    }
}
