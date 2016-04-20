package ca.umontreal.ift2905.nuteme;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.GenericAggregation;
import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.IngredientNutrients;
import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.IngredientRecipes;
import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.NutrientIngredients;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;
import ca.umontreal.ift2905.nuteme.DataModel.SimpleRecipe;

/**
 * Created by h on 06/04/16.
 */
public class NutrientExpandableListFragment extends Fragment {

    ExpandableListView listView;
    DetailedViews parentActivity;

    NutrientIngredients aggregatedData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentActivity = (DetailedViews) getActivity();

        Bundle args = getArguments();
        Gson gson = new Gson();
        Type type = new TypeToken<NutrientIngredients>() {}.getType();
        String json = args.getString(NutrientsTabPagerFragment.NUTRIENT_INGREDIENTS_JSON);
        aggregatedData = gson.fromJson(json, type);


        View v = inflater.inflate(R.layout.nutrients_details, container, false);
        listView = (ExpandableListView)v.findViewById(R.id.nutrients_expandableListView);

        ListAdapter adapter = new ListAdapter();
        listView.setAdapter(adapter);

        return v;
    }

    public class ListAdapter extends BaseExpandableListAdapter {

        //Context ctx;
        LayoutInflater inflater;

        List<IngredientRecipes> listData;


        public ListAdapter(){
            //this.ctx = context;
            inflater = (LayoutInflater)parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listData = aggregatedData.aggregatedList;
        }

        @Override
        public int getGroupCount() {
            return listData.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return listData.get(groupPosition).aggregatedList.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = inflater.inflate(R.layout.nutrients_listheader, parent, false);
            }
            TextView tv = (TextView)convertView.findViewById(R.id.nutrients_header_text);

            tv.setText(listData.get(groupPosition).name);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = inflater.inflate(R.layout.nutrients_listitem, parent, false);
            }
            TextView tv = (TextView)convertView.findViewById(R.id.nutrients_item_recipe);
            String name = listData.get(groupPosition).aggregatedList.get(childPosition).name;

            tv.setText(name);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

}
