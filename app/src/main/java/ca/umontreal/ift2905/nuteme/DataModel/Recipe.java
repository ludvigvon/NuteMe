package ca.umontreal.ift2905.nuteme.DataModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by h on 25/03/16.
 */
public class Recipe {
    public int id;
    public String title;
    public String image;

    public int servings;
    public int preparationMinutes;
    public int cookingMinutes;
    public int readyInMinutes;

    public List<Ingredient> extendentIngredients;

    public Nutrition nutrition;




}
