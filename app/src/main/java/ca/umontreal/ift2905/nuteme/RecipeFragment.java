package ca.umontreal.ift2905.nuteme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import ca.umontreal.ift2905.nuteme.DataAccess.APIHelper;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;

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
            recipe.title = args.getString(RecipesPagerFragment.TITLE);
            recipe.image = args.getString(RecipesPagerFragment.URL);
            //String sdesc =args.getString(RecipesPagerFragment.DESC);
            populateFields();
        }
        else if (getActivity().getClass().getSimpleName().equals(RecipeDescription.class.getSimpleName())){
            Intent intent = ((RecipeDescription)getActivity()).getIntent();
            recipe.id = intent.getIntExtra(getString(R.string.RECIPE_ID), 0);
            if(APIHelper.isNetworkAvailable(getActivity())) {
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
        ImageView img = (ImageView) v.findViewById(R.id.recipe_image_details);
        TextView title = (TextView) v.findViewById(R.id.recipe_title_details);


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
}
//
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//    @Override
//    protected void onPostExecute(RecipeRatio result) {
//        //Toast.makeText(Favorites.this, "new favorites: ", Toast.LENGTH_SHORT).show();
////            recipe = result;
////            PopulateFields();
//    }
//
//    @Override
//    protected RecipeRatio doInBackground(Void... params) {
//
//    }