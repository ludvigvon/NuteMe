package ca.umontreal.ift2905.nuteme.BusinessLogic;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.umontreal.ift2905.nuteme.DataModel.Ingredient;
import ca.umontreal.ift2905.nuteme.DataModel.Nutrient;
import ca.umontreal.ift2905.nuteme.DataModel.Nutrition;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;

/**
 * Created by h on 25/03/16.
 */
public class DataAggregator {

    /**
     * Aggregates nutritional values for a list of recipes
     * @param recipes A list of recipes with nutritional data
     * @return List<Nutrient> Aggregated list of nutrients ordered by nutrient name
     */
    public List<Nutrient> getSummaryAggregateView(List<Recipe> recipes){
        Map<String, Nutrient> dict = new HashMap<>();

        for (Recipe recipe : recipes){
            for (Nutrient nutrient : recipe.nutrition.nutrients){
                String key = nutrient.title;
                if (dict.containsKey(key)){
                    Nutrient value = dict.get(key);
                    if (value.unit == nutrient.unit)
                        value.amount += nutrient.amount;
                    else
                        Log.d("DataAggregator", "Unit mismatch in getSummaryAggregateView");
                }
                else{
                    dict.put(key, nutrient);
                }
            }
        }

        List<Nutrient> result = new ArrayList<>(dict.values());

        Collections.sort(result, new Comparator<Nutrient>() {
            @Override
            public int compare(Nutrient lhs, Nutrient rhs) {
                return lhs.title.compareTo(rhs.title);
            }
        });

        return  result;
    }

    /* TODO: ajouter une méthode qui reçoit une liste de recettes fait une agrégation sur les
           ingrédients et retourne une liste d'ingrédients. Pour chaque ingrédient, on veut avoir
           le ratio de chacune des recettes et les principaux éléments nutritionnels
    */

    /**
     *
     * @param recipes
     * @return
     */
    public List<Ingredient> getAggregateViewByIngredient(List<Recipe> recipes){
        return new ArrayList<Ingredient>();
    }

    /* TODO: ajouter une méthode qui reçoit une liste de recettes fait une agrégation sur les
           nutriments et retourne une liste de nutriments. Pour chaque nutriment, on veut avoir
           les principaux ingrédients qui participents à leur valeur
    */

    /**
     *
     * @param recipes
     * @return
     */
    public List<Nutrient> getAggregateViewByNutrient(List<Recipe> recipes){
        return new ArrayList<Nutrient>();
    }
}
