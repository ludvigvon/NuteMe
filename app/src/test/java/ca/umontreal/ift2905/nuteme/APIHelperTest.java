package ca.umontreal.ift2905.nuteme;

import android.util.Log;

import org.junit.Test;

import java.util.List;

import ca.umontreal.ift2905.nuteme.DataAccess.APIHelper;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;
import ca.umontreal.ift2905.nuteme.DataModel.SimpleRecipe;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class APIHelperTest {

    @Test
    public void getRecipe_Success() throws Exception {
        int recipeId = 156992;
        APIHelper helper = new APIHelper();

        Recipe recipe = helper.getRecipe(recipeId);

        assertNotNull(recipe);
        assertEquals(recipeId, recipe.id);
    }

    @Test
    public void seachRecipes_Success() throws Exception {
        String keyWord = "chicken";
        APIHelper helper = new APIHelper();

        List<SimpleRecipe> recipes = helper.seachRecipes(keyWord);

        assertNotNull(recipes);
        assertTrue(recipes.size() > 0);
    }
}