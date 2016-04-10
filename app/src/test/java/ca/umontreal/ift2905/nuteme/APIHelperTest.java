package ca.umontreal.ift2905.nuteme;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ca.umontreal.ift2905.nuteme.DataAccess.APIHelper;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;
import ca.umontreal.ift2905.nuteme.DataModel.Recipes;
import ca.umontreal.ift2905.nuteme.DataModel.SimpleRecipe;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class APIHelperTest {

    @Test
    public void getRecipe_Success() throws Exception {
        int recipeId = 156992;
        APIHelper helper = APIHelper.getInstance();


        Recipe recipe = helper.getRecipe(recipeId);

        assertNotNull(recipe);
        assertEquals(recipeId, recipe.id);
    }

    @Test
    public void getRecipe_InvalidID() throws Exception {
        int recipeId = 1;
        int resulId = 0;
        APIHelper helper = APIHelper.getInstance();

        Recipe recipe = helper.getRecipe(recipeId);

        assertNotNull(recipe);
        assertEquals(resulId, recipe.id);
    }

    @Test
    public void getDetailedRecipes_Success() throws Exception {
        int recipeId = 156992;
        List<Integer> ids = new ArrayList<>();
        ids.add(new Integer(recipeId));

        APIHelper helper = APIHelper.getInstance();

        List<Recipe> recipes = helper.getDetailedRecipes(ids);

        assertNotNull(recipes);
        assertTrue(recipes.size() > 0);
        assertEquals(recipeId, recipes.get(0).id);
    }

    @Test
    public void getDetailedRecipes_NullID() throws Exception {
        List<Integer> ids = new ArrayList<>();
        ids.add(null);

        APIHelper helper = APIHelper.getInstance();

        List<Recipe> recipes = helper.getDetailedRecipes(ids);

        assertNotNull(recipes);
        assertTrue(recipes.size() == 0);
    }


    @Test
    public void seachRecipes_Success() throws Exception {
        String keyWord = "chicken";
        APIHelper helper = APIHelper.getInstance();

        Recipes recipes = helper.seachRecipes(keyWord);

        assertNotNull(recipes);
        assertTrue(recipes.results.size() > 0);
    }
}