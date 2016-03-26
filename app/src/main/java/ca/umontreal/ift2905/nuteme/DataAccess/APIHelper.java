package ca.umontreal.ift2905.nuteme.DataAccess;

import android.content.Context;
import android.provider.DocumentsContract;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import ca.umontreal.ift2905.nuteme.DataModel.Recipe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by h on 21/03/16.
 */
public class APIHelper {

    public APIHelper() {
    }

    public Recipe getRecipe(int id) throws IOException {
        // get json
        String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" + id + "/information?includeNutrition=false";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("X-Mashape-Key", "vAqNvzgwvdmshYrsPdGjebs8052qp17L6B6jsnJnkEy2zDMHlG").build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();

        // parse json
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Recipe> jsonAdapter = moshi.adapter(Recipe.class);
        Recipe recipe = jsonAdapter.fromJson(json);

        return recipe;
    }

}
