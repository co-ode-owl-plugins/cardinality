package org.coode.cardinality.prefs;

import org.coode.cardinality.ui.CardinalityView;
import org.protege.editor.core.prefs.Preferences;
import org.protege.editor.core.prefs.PreferencesManager;

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
 * http://www.cs.man.ac.uk/~drummond<br>
 * <br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Aug 30, 2006<br>
 * <br>
 * <p/>
 */
public class CardiPrefs {

    private static CardiPrefs instance;
    private final Preferences prefs;
    public static final String OPT_COMPLEX_FILLERS_ALLOWED = "opt_complex_fillers_allowed";
    public static final String OPT_SHOW_INHERITED_RESTRS = "opt_show_inherited_restrs";
    public static final String OPT_DOUBLE_CLICK_NAV = "opt_double_click_nav";
    public static final String OPT_EDIT_DT_PROPERTIES = "opt_edit_dt_properties";
    public static final String OPT_CREATE_PROPERTIES_INLINE = "opt_create_properties_inline";
    public static final String FILLER_COL_WIDTH = "filler_col_width";
    public static final String PROP_COL_WIDTH = "prop_col_width";

    public static Preferences getInstance() {
        if (instance == null) {
            instance = new CardiPrefs();
        }
        return instance.prefs;
    }

    private CardiPrefs() {
        prefs = PreferencesManager.getInstance().getPreferencesForSet(
                "org.coode.cardinality", CardinalityView.class);
    }
}
