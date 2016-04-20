package ca.umontreal.ift2905.nuteme.Utilities;

import java.math.BigDecimal;

/**
 * Created by h on 19/04/16.
 */
public class Format {

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static String formatUnit(String unit) {
        if (unit != null && (unit.equals("Tablespoons") || unit.equals("Tablespoon")))
            unit = "tbsp";
        return unit;
    }


}
