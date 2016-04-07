package ca.umontreal.ift2905.nuteme;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import ca.umontreal.ift2905.nuteme.DataAccess.APIHelper;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;

public class DetailedViews extends AppCompatActivity  {

    TabLayout tablayout;
    ViewPager pager;
    List<Recipe> recipesData;

    public static final String[] TAB_TITLES = new String[] {"Recipes", "Ingredients", "Nutrients"};
    public static final String DETAILED_VIEWS_POS = "DetailedViewsPosition";
    //public static final String RECIPES = "RecipesJson";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_views_activity);

        tablayout = (TabLayout)findViewById(R.id.detailedViews_tablayout);
        pager = (ViewPager)findViewById(R.id.detailedViews_pager);

        ApiAsyncTask run = new ApiAsyncTask();
        run.execute();
    }

    public List<Recipe> getRecipes(){
        return recipesData;
    }

    public class ApiAsyncTask extends AsyncTask<String, String, List<Recipe>> {

        public ApiAsyncTask() {}

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

            recipesData = recipes;

            DetailedViewsPagerAdapter adapter = new DetailedViewsPagerAdapter(getSupportFragmentManager());
            pager.setAdapter(adapter);

            tablayout.setupWithViewPager(pager);
        }

    }


    public class DetailedViewsPagerAdapter extends FragmentPagerAdapter {

        public DetailedViewsPagerAdapter(FragmentManager fm) {super(fm);}

        @Override
        public Fragment getItem(int position) {

//            Gson gson = new Gson();
//            Type type = new TypeToken<List<Recipe>>() {}.getType();
//            String json = gson.toJson(recipes, type);

//            Bundle bundle = new Bundle();
//            bundle.putString(RECIPES, json);

            Fragment fragment = null;
            switch(position){
                case 0:
                    fragment = new RecipesPagerFragment();
                    break;
                case 1:
                    fragment = new IngredientsFragment();
                    break;
                case 2:
                    fragment = new NutrientsTabPagerFragment();
                    break;
            }

//            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return TAB_TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }
    }

}
