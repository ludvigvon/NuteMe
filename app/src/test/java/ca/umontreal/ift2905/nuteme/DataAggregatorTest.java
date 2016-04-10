package ca.umontreal.ift2905.nuteme;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ca.umontreal.ift2905.nuteme.BusinessLogic.DataAggregator;
import ca.umontreal.ift2905.nuteme.DataAccess.APIHelper;
import ca.umontreal.ift2905.nuteme.DataModel.Nutrient;
import ca.umontreal.ift2905.nuteme.DataModel.Nutrition;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;
import ca.umontreal.ift2905.nuteme.DataModel.Recipes;

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

    @Test
    public void getRecipe_Success() throws Exception {

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


}