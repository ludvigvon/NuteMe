package ca.umontreal.ift2905.nuteme.DataAccess;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.umontreal.ift2905.nuteme.DataModel.Recipe;
import ca.umontreal.ift2905.nuteme.DataModel.Recipes;
import ca.umontreal.ift2905.nuteme.DataModel.SimpleRecipe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by h on 21/03/16.
 */
public class APIHelper {

    private static APIHelper instance = null;
    private List<Recipe> recipes = null;

    private static final String X_MASHAPE_KEY = "X-Mashape-Key";
    private static final String X_MASHAPE_VALUE = "vAqNvzgwvdmshYrsPdGjebs8052qp17L6B6jsnJnkEy2zDMHlG";
    private static final String RECIPE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/%s/information?includeNutrition=%s";
    private static final String SEARCH_RECIPES_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?query=%s";

    protected APIHelper() {
    }

    public static APIHelper getInstance() {
        if (instance == null){
            instance = new APIHelper();
        }
        return instance;
    }

    // prefix image url with https://spoonacular.com/recipeImages/
    public Recipes seachRecipes(String keyWord) throws IOException {
        // get json
        String url = String.format(SEARCH_RECIPES_URL, keyWord);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).header(X_MASHAPE_KEY, X_MASHAPE_VALUE).build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();


        // parse json
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Recipes> jsonAdapter = moshi.adapter(Recipes.class);

        return jsonAdapter.fromJson(json);
    }

    public SimpleRecipe getRecipeSummary(int id) throws IOException {
        String json = getRecipeJson(id, false);

        // parse json
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SimpleRecipe> jsonAdapter = moshi.adapter(SimpleRecipe.class);
        SimpleRecipe recipe = jsonAdapter.fromJson(json);

        return recipe;
    }

    public Recipe getRecipe(int id) throws IOException {
        return getRecipe(id, false);
    }

    private Recipe getRecipe(int id, boolean includeNutrition) throws IOException {
        String json = getRecipeJson(id, includeNutrition);

        // parse json
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Recipe> jsonAdapter = moshi.adapter(Recipe.class);
        Recipe recipe = jsonAdapter.fromJson(json);

        return recipe;
    }

    @NonNull
    private String getRecipeJson(int id, boolean includeNutrition) throws IOException {
        // get json
        String url = String.format(RECIPE_URL, id, (includeNutrition ? "true" : "false"));
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).header(X_MASHAPE_KEY, X_MASHAPE_VALUE).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public List<Recipe> getDetailedRecipes(){        
        return recipes;
    }



    public List<Recipe> getDetailedRecipes(List<Integer> ids) {
//        Log.d("API", "Going to api server!!!");
//        if (recipes!= null)
//            Log.d("API", "Before initialize - Recipes count: " + recipes.size());
//        else
//            Log.d("API", "Before initialize - Recipes is null");
        recipes = new ArrayList<>();
//        Log.d("API", "After initialize - Recipes count: " + recipes.size());

        Recipe recipe;
        for (Integer id : ids) {
            try {
                if (id != null) {
                    recipe = getRecipe(id, true);
                    if (recipe != null && recipe.id != 0) {
                        recipes.add(recipe);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return recipes;
    }




}
