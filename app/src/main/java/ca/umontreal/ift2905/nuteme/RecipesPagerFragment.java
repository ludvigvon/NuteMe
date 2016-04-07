package ca.umontreal.ift2905.nuteme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ca.umontreal.ift2905.nuteme.DataModel.Recipe;

/**
 * Created by h on 06/04/16.
 */
public class RecipesPagerFragment extends Fragment {

    public static final String TITLE = "TITLE";
    public static final String URL = "URL";
    public static final String DESC = "DESC";

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
