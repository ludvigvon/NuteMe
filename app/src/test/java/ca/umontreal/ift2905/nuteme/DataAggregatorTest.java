package ca.umontreal.ift2905.nuteme;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ca.umontreal.ift2905.nuteme.BusinessLogic.DataAggregator;
import ca.umontreal.ift2905.nuteme.DataAccess.APIHelper;
import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.GenericAggregation;
import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.IngredientNutrients;
import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.NutrientIngredients;
import ca.umontreal.ift2905.nuteme.DataModel.Ingredient;
import ca.umontreal.ift2905.nuteme.DataModel.Nutrient;
import ca.umontreal.ift2905.nuteme.DataModel.Nutrition;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;
import ca.umontreal.ift2905.nuteme.DataModel.Recipes;
import ca.umontreal.ift2905.nuteme.DataModel.SimpleNutrient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class DataAggregatorTest {

    public List<Nutrient> getMockListNutrients(){
        List<Nutrient> nutrients = new ArrayList<>();
        Nutrient nutrient1 = new Nutrient();
        Nutrient nutrient2 = new Nutrient();
        Nutrient nutrient3 = new Nutrient();

        nutrient1.title = "nutrient1";
        nutrient1.amount = 10;
        nutrient1.unit = "mg";

        nutrient2.title = "nutrient2";
        nutrient2.amount = 20;
        nutrient2.unit = "mg";

        nutrient3.title = "nutrient3";
        nutrient3.amount = 30;
        nutrient3.unit = "mg";

        nutrients.add(nutrient1);
        nutrients.add(nutrient2);
        nutrients.add(nutrient3);

        return nutrients;
    }

    public List<SimpleNutrient> getMockListSimpleNutrients(int[] vals){
        List<SimpleNutrient> nutrients = new ArrayList<>();
        SimpleNutrient nutrient1 = new SimpleNutrient();
        SimpleNutrient nutrient2 = new SimpleNutrient();
        SimpleNutrient nutrient3 = new SimpleNutrient();

        nutrient1.name = "nutrient1";
        nutrient1.amount = vals[0];
        nutrient1.unit = "mg";

        nutrient2.name = "nutrient2";
        nutrient2.amount = vals[1];
        nutrient2.unit = "mg";

        nutrient3.name = "nutrient3";
        nutrient3.amount = vals[2];
        nutrient3.unit = "mg";

        nutrients.add(nutrient1);
        nutrients.add(nutrient2);
        nutrients.add(nutrient3);

        return nutrients;
    }

    public List<Ingredient> getMockListIngredients(){
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient();
        Ingredient ingredient2 = new Ingredient();
        Ingredient ingredient3 = new Ingredient();

        ingredient1.name = "ingredient1";
        ingredient1.amount = 10;
        ingredient1.unit = "mg";
        ingredient1.nutrients = getMockListSimpleNutrients(new int[]{2,3,5});

        ingredient2.name = "ingredient2";
        ingredient2.amount = 20;
        ingredient2.unit = "mg";
        ingredient2.nutrients = getMockListSimpleNutrients(new int[]{3,5,12});

        ingredient3.name = "ingredient3";
        ingredient3.amount = 30;
        ingredient3.unit = "mg";
        ingredient3.nutrients = getMockListSimpleNutrients(new int[]{8,10,12});

        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        ingredients.add(ingredient3);

        return ingredients;
    }

    @Test
    public void getSummaryAggregateView_Success() throws Exception {

        List<Recipe> recipes = new ArrayList<>();

        Recipe recipe1 = new Recipe();
        Recipe recipe2 = new Recipe();
        Recipe recipe3 = new Recipe();

        recipe1.nutrition = new Nutrition();
        recipe2.nutrition = new Nutrition();
        recipe3.nutrition = new Nutrition();

        recipe1.nutrition.nutrients = getMockListNutrients();
        recipe2.nutrition.nutrients = getMockListNutrients();
        recipe3.nutrition.nutrients = getMockListNutrients();

        recipes.add(recipe1);
        recipes.add(recipe2);
        recipes.add(recipe3);

        DataAggregator aggregator = new DataAggregator();

        List<Nutrient> result = aggregator.getSummaryAggregateView(recipes);


        Assert.assertTrue(result.get(0).amount == 30);
        Assert.assertTrue(result.get(1).amount == 60);
        Assert.assertTrue(result.get(2).amount == 90);

    }

    @Test
    public void getAggregateViewByIngredient_Success() throws Exception {


        List<Recipe> recipes = new ArrayList<>();

        Recipe recipe1 = new Recipe();
        Recipe recipe2 = new Recipe();
        Recipe recipe3 = new Recipe();

        recipe1.title = "recipe1";
        recipe2.title = "recipe2";
        recipe3.title = "recipe3";

        recipe1.nutrition = new Nutrition();
        recipe2.nutrition = new Nutrition();
        recipe3.nutrition = new Nutrition();

        recipe1.nutrition.ingredients = getMockListIngredients();
        recipe2.nutrition.ingredients = getMockListIngredients();
        recipe3.nutrition.ingredients = getMockListIngredients();

        recipes.add(recipe1);
        recipes.add(recipe2);
        recipes.add(recipe3);

        DataAggregator aggregator = new DataAggregator();

        GenericAggregation<IngredientNutrients> result = aggregator.getAggregateViewByIngredient(recipes);


        Assert.assertNotNull(result);

    }

    @Test
    public void getAggregateViewByNutrient_Success() throws Exception {


        List<Recipe> recipes = new ArrayList<>();

        Recipe recipe1 = new Recipe();
        Recipe recipe2 = new Recipe();
        Recipe recipe3 = new Recipe();

        recipe1.title = "recipe1";
        recipe2.title = "recipe2";
        recipe3.title = "recipe3";

        recipe1.nutrition = new Nutrition();
        recipe2.nutrition = new Nutrition();
        recipe3.nutrition = new Nutrition();

        recipe1.nutrition.ingredients = getMockListIngredients();
        recipe2.nutrition.ingredients = getMockListIngredients();
        recipe3.nutrition.ingredients = getMockListIngredients();

        recipes.add(recipe1);
        recipes.add(recipe2);
        recipes.add(recipe3);

        DataAggregator aggregator = new DataAggregator();

        GenericAggregation<NutrientIngredients> result = aggregator.getAggregateViewByNutrient(recipes);


        Assert.assertNotNull(result);

    }

}