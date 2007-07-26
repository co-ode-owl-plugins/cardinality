package org.coode.cardinality.prefs;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Author: drummond<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 29, 2006<br><br>
 * <p/>
 * nick.drummond@cs.manchester.ac.uk<br>
 * www.cs.man.ac.uk/~drummond<br><br>
 */
public class CardinalityProperties extends Properties {

    private static CardinalityProperties instance;
    private static final String PROPS_FILENAME = "cardinality.properties";


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
