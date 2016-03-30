package ca.umontreal.ift2905.nuteme;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailedViews extends AppCompatActivity  {

    TabLayout tablayout;
    ViewPager pager;
    PagerAdapter adapter;

    public static final String[] TAB_TITLES = new String[] {"Recipes", "Ingredients", "Nutrients"};
    public static final String DETAILED_VIEWS_POS = "DetailedViewsPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_views);

        tablayout = (TabLayout)findViewById(R.id.tablayout_detailedViews);
        pager = (ViewPager)findViewById(R.id.pager_detailedViews);

        adapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        tablayout.setupWithViewPager(pager);

    }

    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle args;

            fragment = new DetailedViewsFragment();
            args = new Bundle();
            args.putInt(DETAILED_VIEWS_POS, position);
            fragment.setArguments(args);

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
