package ca.umontreal.ift2905.nuteme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ca.umontreal.ift2905.nuteme.DataModel.Recipe;

/**
 * Created by h on 26/03/16.
 */
public class NutrientsTabPagerFragment extends Fragment {

    public static final String TITLE = "TITLE";
    public static final String URL = "URL";
    public static final String DESC = "DESC";

    TabLayout tablayout;
    ViewPager pager;
    DetailedViews parentActivity;
    List<Recipe> recipes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.nutrients_fragment, container, false);

        tablayout = (TabLayout)v.findViewById(R.id.nutrients_tablayout);
        pager = (ViewPager)v.findViewById(R.id.nutrients_pager);

        parentActivity = (DetailedViews) getActivity();

        recipes = parentActivity.getRecipes();

        NutrientsStatePagerAdapter adapter = new NutrientsStatePagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);

        tablayout.setupWithViewPager(pager);

        return v;
    }

    public class NutrientsStatePagerAdapter extends FragmentStatePagerAdapter {

        Recipe testRecipe = recipes.get(0);

        public NutrientsStatePagerAdapter(FragmentManager fm) { super(fm); }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            fragment = new NutrientExpandableListFragment();

            return fragment;
        }

        @Override
        public int getCount() {
            return testRecipe.nutrition.nutrients.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return testRecipe.nutrition.nutrients.get(position).title;
        }
    }
}
