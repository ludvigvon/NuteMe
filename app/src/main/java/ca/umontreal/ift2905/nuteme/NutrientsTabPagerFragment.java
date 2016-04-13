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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ca.umontreal.ift2905.nuteme.BusinessLogic.DataAggregator;
import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.GenericAggregation;
import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.NutrientIngredients;

/**
 * Created by h on 26/03/16.
 */
public class NutrientsTabPagerFragment extends Fragment {

    public static final String NUTRIENT_INGREDIENTS_JSON = "NutrientIngredients";


    TabLayout tablayout;
    ViewPager pager;
    DetailedViews parentActivity;

    GenericAggregation<NutrientIngredients> aggregatedData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.nutrients_fragment, container, false);

        tablayout = (TabLayout)v.findViewById(R.id.nutrients_tablayout);
        pager = (ViewPager)v.findViewById(R.id.nutrients_pager);

        parentActivity = (DetailedViews) getActivity();

        aggregatedData = new DataAggregator().getAggregateViewByNutrient(parentActivity.getRecipes());

        NutrientsStatePagerAdapter adapter = new NutrientsStatePagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);

        tablayout.setupWithViewPager(pager);

        return v;
    }

    public class NutrientsStatePagerAdapter extends FragmentStatePagerAdapter {

        List<NutrientIngredients> listData;

        public NutrientsStatePagerAdapter(FragmentManager fm) {
            super(fm);
            listData = aggregatedData.aggregationList;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new NutrientExpandableListFragment();;

            NutrientIngredients aggregatedData = listData.get(position);

            Gson gson = new Gson();
            Type type = new TypeToken<NutrientIngredients>() {}.getType();
            String json = gson.toJson(aggregatedData, type);

            Bundle bundle = new Bundle();
            bundle.putString(NUTRIENT_INGREDIENTS_JSON, json);
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listData.get(position).name;
        }
    }
}
