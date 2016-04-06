package ca.umontreal.ift2905.nuteme;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ca.umontreal.ift2905.nuteme.DataAccess.APIHelper;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;

/**
 * Created by h on 06/04/16.
 */
public class RecipePagerFragment extends Fragment {

    public static final String TITLE = "TITLE";
    public static final String URL = "URL";
    public static final String DESC = "DESC";

    ViewPager pager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = null;
        List<Recipe> recipes = null;

        Bundle args = getArguments();
        String json = args.getString(DetailedViews.RECIPES);

        Gson gson = new Gson();
        Type type = new TypeToken<List<Recipe>>() {}.getType();
        recipes = gson.fromJson(json, type);

        for (Recipe recipe : recipes) {
            Log.d("Json", recipe.title);
        }


        v = inflater.inflate(R.layout.fragment_recipes, container, false);
        pager = (ViewPager)v.findViewById(R.id.pager_detailedRecipes);

        DynamicPagerAdapter adapter = new DynamicPagerAdapter(getChildFragmentManager(), recipes);
        pager.setAdapter(adapter);

        return v;

    }

    public class DynamicPagerAdapter extends FragmentPagerAdapter {
        private List<Recipe> recipes;

        public DynamicPagerAdapter(FragmentManager fm, List<Recipe> recipes) {
            super(fm);
            this.recipes = recipes;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            fragment = new RecipeFragment();
            String title = recipes.get(position).title;
            String url = recipes.get(position).image;
            //String desc = recipes.get(position).

            Bundle bundle = new Bundle();
            bundle.putString(TITLE, title);
            bundle.putString(URL, url);
            //bundle.putString(DESC, desc);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return recipes.size();
        }
    }

}
