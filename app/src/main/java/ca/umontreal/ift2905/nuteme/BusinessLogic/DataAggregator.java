package ca.umontreal.ift2905.nuteme.BusinessLogic;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.GenericAggregation;
import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.IngredientNutrients;
import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.NutrientRecipes;
import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.RecipeRatio;
import ca.umontreal.ift2905.nuteme.DataModel.Ingredient;
import ca.umontreal.ift2905.nuteme.DataModel.Nutrient;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;
import ca.umontreal.ift2905.nuteme.DataModel.SimpleNutrient;

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

    /**
     *
     * @param recipes
     * @return
     */
    public GenericAggregation<IngredientNutrients> getAggregateViewByIngredient(List<Recipe> recipes){
        Map<String, List<IngredientNutrients>> dict = new HashMap<>();

        // TODO: (Hani) add if > 0

        for (Recipe recipe : recipes){
            for (Ingredient ingredient : recipe.nutrition.ingredients){
                String key = ingredient.name;
                if (dict.containsKey(key)){
                    List<IngredientNutrients> value = dict.get(key);
                    mapIngredientNutrients(recipe, ingredient, value);
                }
                else{
                    List<IngredientNutrients> list = new ArrayList<IngredientNutrients>();
                    mapIngredientNutrients(recipe, ingredient, list);
                    dict.put(key, list);
                }
            }
        }

        // fusionner les listes pour un même ingrédient
        for (Map.Entry<String, List<IngredientNutrients>> entry : dict.entrySet() ){
            Map<String, NutrientRecipes> nutrientRecipesDict = new HashMap<>();

            List<IngredientNutrients> value = entry.getValue();
            IngredientNutrients first = value.get(0);

            for (int i=1; i < value.size()   ; i++){
                IngredientNutrients next = value.get(i);
                if (next.unit == first.unit) {
                    first.amount += next.amount;
                    first.aggregatedList.addAll(next.aggregatedList);

                    for (NutrientRecipes nutrientRecipes : first.aggregatedList){
                        String key = nutrientRecipes.name;
                        if (nutrientRecipesDict.containsKey(key)){
                            NutrientRecipes nutrientRecipesValue = nutrientRecipesDict.get(key);
                            if (nutrientRecipesValue.unit == nutrientRecipes.unit) {
                                nutrientRecipesValue.amount += nutrientRecipes.amount;
                                nutrientRecipesValue.aggregatedList.addAll(nutrientRecipes.aggregatedList);
                            }
                            else
                                Log.d("DataAggregator", "Unit mismatch in getSummaryAggregateView");
                        }
                        else{
                            nutrientRecipesDict.put(key,nutrientRecipes);
                        }
                    }
                }
                else
                    Log.d("DataAggregator", "Unit mismatch in getSummaryAggregateView");
            }
            first.aggregatedList = new ArrayList<>(nutrientRecipesDict.values());
        }

        // clean up


        List<List<IngredientNutrients>> result = new ArrayList<>(dict.values());


        for (List<IngredientNutrients> list : result){

        }


        /* from this list of lists of IngredientNutrients compute a single list of  IngredientNutrients encapsulated in GenericAggregation<IngredientNutrients> */


//        Collections.sort(result, new Comparator<Nutrient>() {
//            @Override
//            public int compare(Nutrient lhs, Nutrient rhs) {
//                return lhs.title.compareTo(rhs.title);
//            }
//        });

        return  new GenericAggregation<>(result.get(0));
    }

    private void mapIngredientNutrients(Recipe recipe, Ingredient ingredient, List<IngredientNutrients> list) {
        IngredientNutrients item = new IngredientNutrients();
        list.add(item);
        item.name = ingredient.name;
        item.amount = ingredient.amount;
        item.aggregatedList = new ArrayList<NutrientRecipes>();
        for (SimpleNutrient nutrient : ingredient.nutrients){
            NutrientRecipes nr = new NutrientRecipes();
            nr.name = nutrient.name;
            nr.amount = nutrient.amount;
            nr.unit = nutrient.unit;
            nr.aggregatedList = new ArrayList<RecipeRatio>();

            RecipeRatio ratio = new RecipeRatio();
            ratio.name = recipe.title;

            item.aggregatedList.add(nr);
            nr.aggregatedList.add(ratio);
        }
    }

    /**
     *
     * @param recipes
     * @return
     */
    public List<Nutrient> getAggregateViewByNutrient(List<Recipe> recipes){
        return new ArrayList<Nutrient>();
    }
}
