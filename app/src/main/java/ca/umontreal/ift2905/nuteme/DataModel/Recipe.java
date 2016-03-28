package ca.umontreal.ift2905.nuteme.DataModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by h on 25/03/16.
 */
public class Recipe extends SimpleRecipe {
    public int servings;
    public int preparationMinutes;
    public int cookingMinutes;

    public List<Ingredient> extendedIngredients;

    public Nutrition nutrition;
}
