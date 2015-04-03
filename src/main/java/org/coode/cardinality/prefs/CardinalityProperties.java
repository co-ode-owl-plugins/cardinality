package org.coode.cardinality.prefs;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

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
 * http://www.cs.man.ac.uk/~drummond<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Aug 30, 2006<br><br>
 * <p/>
 */
public class CardinalityProperties extends Properties {

    private static final String PROPS_FILENAME = "resources/cardinality.properties";
    
    private static CardinalityProperties instance;

    public static CardinalityProperties getInstance() {
        if (instance == null) {
            instance = new CardinalityProperties();
            try {
                instance.load(instance.getClass().getClassLoader().getResourceAsStream(PROPS_FILENAME));
            }
            catch (IOException e) {
                Logger.getLogger(CardinalityProperties.class).error(e);
            }
        }
        return instance;
    }
}
