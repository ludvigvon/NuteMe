package ca.umontreal.ift2905.nuteme;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import ca.umontreal.ift2905.nuteme.DataAccess.APIHelper;
import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.IngredientNutrients;
import ca.umontreal.ift2905.nuteme.DataModel.Ingredient;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;
import ca.umontreal.ift2905.nuteme.Utilities.Network;

/**
 * Created by h on 26/03/16.
 */
public class RecipeFragment extends Fragment {

    View v;
    Recipe recipe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.recipes_details, container, false);

        recipe = new Recipe();

        if (getActivity().getClass().getSimpleName().equals(DetailedViews.class.getSimpleName())) {
            Bundle args = getArguments();

            Gson gson = new Gson();
            Type type = new TypeToken<Recipe>() {}.getType();

            String json = args.getString(RecipesPagerFragment.RECIPE_JSON);

            recipe = gson.fromJson(json, type);

            populateFields();
        }
        else if (getActivity().getClass().getSimpleName().equals(RecipeDescription.class.getSimpleName())){
            Intent intent = ((RecipeDescription)getActivity()).getIntent();
            recipe.id = intent.getIntExtra(getString(R.string.RECIPE_ID), 0);
            if(Network.isNetworkAvailable(getActivity())) {
                APITask apiTask = new APITask();
                apiTask.execute();
            }
            else{
                Toast.makeText(getActivity(), R.string.Network_unavailable_error, Toast.LENGTH_LONG).show();
            }
        }

        return v;
    }

    private void populateFields() {
        // get reference to visual elements
        ImageView img = (ImageView) v.findViewById(R.id.recipe_image);
        TextView title = (TextView) v.findViewById(R.id.recipe_title);
        ListView ingredients = (ListView)v.findViewById(R.id.recipe_listview_ingredients);

        ingredients.setAdapter(new IngredientsListAdapter());


        // set values to visual elements
        title.setText(recipe.title);
        Picasso.with(getContext()).load(recipe.image).into(img);
    }

    public class APITask extends AsyncTask<Void, Void, Recipe> {

        @Override
        protected void onPostExecute(Recipe result) {
            super.onPostExecute(recipe);
            recipe = result;
            populateFields();
        }

        @Override
        protected Recipe doInBackground(Void... params) {
            APIHelper api = APIHelper.getInstance();
            Recipe result = null;
            try {
                result = api.getRecipe(recipe.id);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    public class IngredientsListAdapter extends BaseAdapter{

        LayoutInflater inflater;
        List<Ingredient> ingredients;

        public IngredientsListAdapter() {
            inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ingredients = recipe.extendedIngredients;
        }

        @Override
        public int getCount() {
            return ingredients.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = inflater.inflate(R.layout.recipes_details_ingredient_listitem, parent, false);
            }

            TextView title = (TextView)v.findViewById(R.id.recipe_ingredient_title);

            Ingredient ing = ingredients.get(position);

            title.setText(ing.originalString);

           return v;
        }
    }
}