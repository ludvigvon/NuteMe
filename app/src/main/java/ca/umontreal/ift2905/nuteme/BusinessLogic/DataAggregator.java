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
import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.IngredientRecipes;
import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.NutrientIngredients;
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
     *
     * @param recipes A list of recipes with nutritional data
     * @return List<Nutrient> Aggregated list of nutrients ordered by nutrient name
     */
    public List<Nutrient> getSummaryAggregateView(List<Recipe> recipes) {
        Map<String, Nutrient> dict = new HashMap<>();

        for (Recipe recipe : recipes) {
            for (Nutrient nutrient : recipe.nutrition.nutrients) {
                String key = nutrient.title;
                if (dict.containsKey(key)) {
                    Nutrient value = dict.get(key);
                    if (value.unit == nutrient.unit)
                        value.amount += nutrient.amount;
                    else
                        Log.d("DataAggregator", "Unit mismatch in getSummaryAggregateView");
                } else {
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

        return result;
    }

    public GenericAggregation<IngredientNutrients> getAggregateViewByIngredient(List<Recipe> recipes) {
        // TODO: (Hani) add if > 0
        Map<String, List<IngredientNutrients>> dictIngredientNutrients = extractIngredients(recipes);

        GenericAggregation<IngredientNutrients> result = new GenericAggregation<>(aggregateIngredientNutrients(dictIngredientNutrients));

        return result;
    }

    private Map<String, List<IngredientNutrients>> extractIngredients(List<Recipe> recipes) {
        Map<String, List<IngredientNutrients>> dict = new HashMap<>();

        for (Recipe recipe : recipes) {
            for (Ingredient ingredient : recipe.nutrition.ingredients) {
                String key = ingredient.name;
                if (dict.containsKey(key)) {
                    List<IngredientNutrients> value = dict.get(key);
                    mapIngredientNutrients(recipe, ingredient, value);
                } else {
                    List<IngredientNutrients> list = new ArrayList<IngredientNutrients>();
                    mapIngredientNutrients(recipe, ingredient, list);
                    dict.put(key, list);
                }
            }
        }
        return dict;
    }

    private void mapIngredientNutrients(Recipe recipe, Ingredient ingredient, List<IngredientNutrients> list) {
        IngredientNutrients item = new IngredientNutrients();
        list.add(item);
        item.name = ingredient.name;
        item.amount = ingredient.amount;
        item.aggregatedList = new ArrayList<NutrientRecipes>();
        for (SimpleNutrient nutrient : ingredient.nutrients){
            if (nutrient.amount > 0) {
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
    }

    private List<IngredientNutrients> aggregateIngredientNutrients(Map<String, List<IngredientNutrients>> dictIngredientNutrients) {
        List<IngredientNutrients> result = new ArrayList<>();

        for (Map.Entry<String, List<IngredientNutrients>> entry : dictIngredientNutrients.entrySet()) {
            Map<String, List<NutrientRecipes>> dictNutrientRecipes = new HashMap<>();

            List<IngredientNutrients> value = entry.getValue();
            IngredientNutrients first = value.get(0);

            result.add(first);

            for (int i = 1; i < value.size(); i++) {
                IngredientNutrients next = value.get(i);
                if (next.unit == first.unit) {
                    first.amount += next.amount;
                    first.aggregatedList.addAll(next.aggregatedList);
                } else
                    Log.d("DataAggregator", "Unit mismatch in getSummaryAggregateView");
            }
            dictNutrientRecipes.clear();
            for (NutrientRecipes nutrientRecipes : first.aggregatedList) {
                String key = nutrientRecipes.name;
                if (dictNutrientRecipes.containsKey(key)) {
                    List<NutrientRecipes> nutrientRecipesList = dictNutrientRecipes.get(key);
                    nutrientRecipesList.add(nutrientRecipes);
                } else {
                    List<NutrientRecipes> listNutrientRecipes = new ArrayList<NutrientRecipes>();
                    listNutrientRecipes.add(nutrientRecipes);
                    dictNutrientRecipes.put(key, listNutrientRecipes);
                }
            }
            first.aggregatedList = aggregateNutrientRecipes(dictNutrientRecipes);
        }

        return result;
    }

    private List<NutrientRecipes> aggregateNutrientRecipes(Map<String, List<NutrientRecipes>> dict) {
        List<NutrientRecipes> result = new ArrayList<>();

        for (Map.Entry<String, List<NutrientRecipes>> entry : dict.entrySet()) {
            Map<String, List<RecipeRatio>> dictRecipeRatio = new HashMap<>();

            List<NutrientRecipes> value = entry.getValue();
            NutrientRecipes first = value.get(0);

            result.add(first);

            for (int i = 1; i < value.size(); i++) {
                NutrientRecipes next = value.get(i);
                if (next.unit == first.unit) {
                    first.amount += next.amount;
                    first.aggregatedList.addAll(next.aggregatedList);
                } else
                    Log.d("DataAggregator", "Unit mismatch in getSummaryAggregateView");
            }
            dictRecipeRatio.clear();
            for (RecipeRatio recipeRatio : first.aggregatedList) {
                String key = recipeRatio.name;
                if (dictRecipeRatio.containsKey(key)) {
                    List<RecipeRatio> recipeRatioList = dictRecipeRatio.get(key);
                    recipeRatioList.add(recipeRatio);
                } else {
                    List<RecipeRatio> recipeRatioList = new ArrayList<RecipeRatio>();
                    recipeRatioList.add(recipeRatio);
                    dictRecipeRatio.put(key, recipeRatioList);
                }
            }

            first.aggregatedList = aggregateRecipeRatios(dictRecipeRatio);

        }

        return result;
    }

    private List<RecipeRatio> aggregateRecipeRatios(Map<String, List<RecipeRatio>> dictRecipeRatio) {
        List<RecipeRatio> result = new ArrayList<>();

        for (Map.Entry<String, List<RecipeRatio>> entry : dictRecipeRatio.entrySet()) {
            Map<String, List<RecipeRatio>> dictRatios = new HashMap<>();

            List<RecipeRatio> value = entry.getValue();
            RecipeRatio first = value.get(0);

            result.add(first);

            for (int i = 1; i < value.size(); i++) {
                RecipeRatio next = value.get(i);
            }
        }
        return result;
    }

    public GenericAggregation<NutrientIngredients>  getAggregateViewByNutrient(List<Recipe> recipes){
        // TODO: (Hani) add if > 0
        Map<String, List<NutrientIngredients>> dictNutrientIngredients = extractNutrients(recipes);

        GenericAggregation<NutrientIngredients> result = new GenericAggregation<>(aggregateNutrientIngredients(dictNutrientIngredients));

        return result;
    }

    private Map<String, List<NutrientIngredients>> extractNutrients(List<Recipe> recipes) {
        Map<String, List<NutrientIngredients>> dict = new HashMap<>();

        for (Recipe recipe : recipes) {
            for (Ingredient ingredient : recipe.nutrition.ingredients) {
                for (SimpleNutrient nutrient : ingredient.nutrients) {
                    if (nutrient.amount>0) {
                        String key = nutrient.name;
                        if (dict.containsKey(key)) {
                            List<NutrientIngredients> value = dict.get(key);
                            mapNutrientIngredients(recipe, ingredient, nutrient, value);
                        } else {
                            List<NutrientIngredients> list = new ArrayList<>();
                            mapNutrientIngredients(recipe, ingredient, nutrient, list);
                            dict.put(key, list);
                        }
                    }
                }
            }
        }
        return dict;
    }

    private void mapNutrientIngredients(Recipe recipe, Ingredient ingredient, SimpleNutrient nutrient, List<NutrientIngredients> list) {
        NutrientIngredients item = new NutrientIngredients();
        list.add(item);
        item.name = nutrient.name;
        item.amount = nutrient.amount;
        item.aggregatedList = new ArrayList<IngredientRecipes>();

        IngredientRecipes ir = new IngredientRecipes();
        ir.name = ingredient.name;
        ir.amount = ingredient.amount;
        ir.unit = ingredient.unit;
        ir.aggregatedList = new ArrayList<RecipeRatio>();

        RecipeRatio ratio = new RecipeRatio();
        ratio.name = recipe.title;

        item.aggregatedList.add(ir);
        ir.aggregatedList.add(ratio);
    }

    private List<NutrientIngredients> aggregateNutrientIngredients(Map<String, List<NutrientIngredients>> dictNutrientIngredients) {
        List<NutrientIngredients> result = new ArrayList<>();

        for (Map.Entry<String, List<NutrientIngredients>> entry : dictNutrientIngredients.entrySet()) {
            Map<String, List<IngredientRecipes>> dictIngredientRecipes = new HashMap<>();

            List<NutrientIngredients> value = entry.getValue();
            NutrientIngredients first = value.get(0);

            result.add(first);

            for (int i = 1; i < value.size(); i++) {
                NutrientIngredients next = value.get(i);
                if (next.unit == first.unit) {
                    first.amount += next.amount;
                    first.aggregatedList.addAll(next.aggregatedList);
                } else
                    Log.d("DataAggregator", "Unit mismatch in getSummaryAggregateView");
            }
            dictIngredientRecipes.clear();
            for (IngredientRecipes ingredientRecipes : first.aggregatedList) {
                String key = ingredientRecipes.name;
                if (dictIngredientRecipes.containsKey(key)) {
                    List<IngredientRecipes> ingredientRecipesList = dictIngredientRecipes.get(key);
                    ingredientRecipesList.add(ingredientRecipes);
                } else {
                    List<IngredientRecipes> listIngredientRecipes = new ArrayList<>();
                    listIngredientRecipes.add(ingredientRecipes);
                    dictIngredientRecipes.put(key, listIngredientRecipes);
                }
            }
            first.aggregatedList = aggregateIngredientRecipes(dictIngredientRecipes);
        }

        return result;
    }

    private List<IngredientRecipes> aggregateIngredientRecipes(Map<String, List<IngredientRecipes>> dict) {
        List<IngredientRecipes> result = new ArrayList<>();

        for (Map.Entry<String, List<IngredientRecipes>> entry : dict.entrySet()) {
            Map<String, List<RecipeRatio>> dictRecipeRatio = new HashMap<>();

            List<IngredientRecipes> value = entry.getValue();
            IngredientRecipes first = value.get(0);

            result.add(first);

            for (int i = 1; i < value.size(); i++) {
                IngredientRecipes next = value.get(i);
                if (next.unit == first.unit) {
                    first.amount += next.amount;
                    first.aggregatedList.addAll(next.aggregatedList);
                } else
                    Log.d("DataAggregator", "Unit mismatch in getSummaryAggregateView");
            }
            dictRecipeRatio.clear();
            for (RecipeRatio recipeRatio : first.aggregatedList) {
                String key = recipeRatio.name;
                if (dictRecipeRatio.containsKey(key)) {
                    List<RecipeRatio> recipeRatioList = dictRecipeRatio.get(key);
                    recipeRatioList.add(recipeRatio);
                } else {
                    List<RecipeRatio> recipeRatioList = new ArrayList<RecipeRatio>();
                    recipeRatioList.add(recipeRatio);
                    dictRecipeRatio.put(key, recipeRatioList);
                }
            }

            first.aggregatedList = aggregateRecipeRatios(dictRecipeRatio);

        }

        return result;
    }

}
