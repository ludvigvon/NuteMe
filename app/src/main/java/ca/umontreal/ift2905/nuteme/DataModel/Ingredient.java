package ca.umontreal.ift2905.nuteme.DataModel;

import java.util.List;

/**
 * Created by h on 27/03/16.
 */
public class Ingredient {
    public String name;
    public float amount;
    public String unit;
    public String unitShort;
    public String unitLong;
    public String originalString;

    public List<SimpleNutrient> nutrients;

}
