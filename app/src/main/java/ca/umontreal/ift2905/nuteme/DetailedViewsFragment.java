package ca.umontreal.ift2905.nuteme;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ca.umontreal.ift2905.nuteme.DataAccess.APIHelper;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;

public class DetailedViewsFragment extends Fragment {

    public static final String TITLE = "TITLE";
    public static final String URL = "URL";
    public static final String DESC = "DESC";

    ViewPager pager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = null;

        Bundle args = getArguments();
        int fragmentId = args.getInt(DetailedViews.DETAILED_VIEWS_POS);

        switch(fragmentId){
            case 0:
                // Inflate the layout for this fragment
                v = inflater.inflate(R.layout.fragment_recipes, container, false);
                pager = (ViewPager)v.findViewById(R.id.pager_detailedRecipes);
                break;
            case 1:
                v = inflater.inflate(R.layout.fragment_ingredients, container, false);
                pager = (ViewPager)v.findViewById(R.id.pager_detailedIngredients);
                break;
            case 2:
                v = inflater.inflate(R.layout.fragment_nutrients, container, false);
                pager = (ViewPager)v.findViewById(R.id.pager_detailedNutrients);
                break;
        }

        ApiAsyncTask run = new ApiAsyncTask(fragmentId);
        run.execute();

        return v;
    }


    public class ApiAsyncTask extends AsyncTask<String, String, List<Recipe>> {

        int fragmentId;

        public ApiAsyncTask(int fragmentId) {
            this.fragmentId = fragmentId;
        }

        @Override
        protected List<Recipe> doInBackground(String... params) {
            List<Recipe> recipes = null;
            APIHelper api = new APIHelper();
            try {
                int[] ids = new int[]{310658, 163336, 203834, 325816, 485365};
                recipes = api.getDetailedRecipes(ids);
            }catch (Exception e){
                Log.d("doInBackground", e.getMessage());
            }

            return recipes;
        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            super.onPostExecute(recipes);

            DynamicPagerAdapter adapter = new DynamicPagerAdapter(getChildFragmentManager(), fragmentId, recipes);
            pager.setAdapter(adapter);
        }
    }

    public class DynamicPagerAdapter extends FragmentPagerAdapter {
        int fragmentId;
        private List<Recipe> recipes;

        public DynamicPagerAdapter(FragmentManager fm, int fragmentId, List<Recipe> recipes) {
            super(fm);
            this.fragmentId = fragmentId;
            this.recipes = recipes;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            switch(fragmentId){
                case 0:
                    fragment = new RecipeFragment();
                    String title = recipes.get(position).title;
                    String url = recipes.get(position).image;
                    //String desc = recipes.get(position).

                    Bundle bundle = new Bundle();
                    bundle.putString(TITLE, title);
                    bundle.putString(URL, url);
                    //bundle.putString(DESC, desc);
                    fragment.setArguments(bundle);
                    break;
                case 1:
                    fragment = new IngredientFragment();
                    String titleTest = "Test Ingredients";
                    //String desc = recipes.get(position).

                    Bundle bundle1 = new Bundle();
                    bundle1.putString(TITLE, titleTest);
                    //bundle.putString(DESC, desc);
                    fragment.setArguments(bundle1);
                    break;
                case 2:
                    fragment = new NutrientFragment();
                    String titleTest2 = "Test Nutrients";
                    //String desc = recipes.get(position).

                    Bundle bundle2 = new Bundle();
                    bundle2.putString(TITLE, titleTest2);
                    //bundle.putString(DESC, desc);
                    fragment.setArguments(bundle2);
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return recipes.size();
        }
    }
}
