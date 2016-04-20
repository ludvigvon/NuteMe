package ca.umontreal.ift2905.nuteme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ca.umontreal.ift2905.nuteme.DataModel.Recipe;
import ca.umontreal.ift2905.nuteme.DataModel.SimpleRecipe;

/**
 * Created by h on 06/04/16.
 */
public class RecipesPagerFragment extends Fragment {

    public static final String RECIPE_JSON = "RecipeJson";

    ViewPager pager;
    DetailedViews parentActivity;
    List<Recipe> recipes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parentActivity = (DetailedViews) getActivity();

        View v = inflater.inflate(R.layout.recipes_fragment, container, false);
        pager = (ViewPager)v.findViewById(R.id.detailedRecipes_page);

        recipes = parentActivity.getRecipes();

        DynamicPagerAdapter adapter = new DynamicPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);

        return v;

    }

    public class DynamicPagerAdapter extends FragmentPagerAdapter {

        public DynamicPagerAdapter(FragmentManager fm) { super(fm); }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            fragment = new RecipeFragment();
            Recipe recipe = recipes.get(position);
            Gson gson = new Gson();
            Type type = new TypeToken<Recipe>() {}.getType();
            String json = gson.toJson(recipe, type);

            Bundle bundle = new Bundle();
            bundle.putString(RECIPE_JSON, json);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return recipes.size();
        }
    }

}
