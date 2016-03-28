package ca.umontreal.ift2905.nuteme.DataAccess;

import android.content.Context;
import android.provider.DocumentsContract;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.umontreal.ift2905.nuteme.DataModel.Recipe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



/**
 * Created by h on 21/03/16.
 */
public class APIHelper {

    private static final String X_MASHAPE_KEY = "vAqNvzgwvdmshYrsPdGjebs8052qp17L6B6jsnJnkEy2zDMHlG";


    public APIHelper() {
    }


    public Recipe getRecipe(int id) throws IOException {
        return getRecipe(id, false);
    }

    private Recipe getRecipe(int id, boolean includeNutrition) throws IOException {
        // get json
        String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" + id + "/information?includeNutrition=" + (includeNutrition ? "true" : "false");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("X-Mashape-Key", X_MASHAPE_KEY).build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();

        Log.d("ID", String.valueOf(id));
        Log.d("jsonApi", json);

        // parse json
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Recipe> jsonAdapter = moshi.adapter(Recipe.class);
        Recipe recipe = jsonAdapter.fromJson(json);

        return recipe;
    }

    public List<Recipe> getDetailedRecipes(int[] ids) {
        Log.d("API", "Going to api server!!!");
        List<Recipe> recipes = new ArrayList<Recipe>();
        Recipe recipe;
        for (int i = 0; i < ids.length; i++) {
            try {
                recipe = getRecipe(ids[i], true);
                recipes.add(recipe);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return recipes;
    }
}
