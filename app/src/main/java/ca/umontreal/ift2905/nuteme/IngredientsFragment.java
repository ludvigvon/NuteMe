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
import android.widget.TextView;

import java.util.List;

import ca.umontreal.ift2905.nuteme.BusinessLogic.DataAggregator;
import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.GenericAggregation;
import ca.umontreal.ift2905.nuteme.DataModel.Aggregations.IngredientNutrients;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;

/**
 * Created by h on 26/03/16.
 */
public class IngredientsFragment extends Fragment {

    ExpandableListView listView;
    DetailedViews parentActivity;
    GenericAggregation<IngredientNutrients> aggregatedData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentActivity = (DetailedViews) getActivity();

        View v = inflater.inflate(R.layout.ingredients_details, container, false);
        listView = (ExpandableListView)v.findViewById(R.id.ingredients_expandableListView);

        aggregatedData = new DataAggregator().getAggregateViewByIngredient(parentActivity.getRecipes());

        ListAdapter adapter = new ListAdapter();
        listView.setAdapter(adapter);

        return v;
    }

    public class ListAdapter extends BaseExpandableListAdapter {

        //Context ctx;
        LayoutInflater inflater;
        List<IngredientNutrients> listData;

        public ListAdapter(){
            //this.ctx = context;
            inflater = (LayoutInflater)parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listData = aggregatedData.aggregationList;
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
                convertView = inflater.inflate(R.layout.ingredients_listheader, parent, false);
            }
            TextView tv = (TextView)convertView.findViewById(R.id.ingredients_header_text);
            tv.setText(listData.get(groupPosition).name);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = inflater.inflate(R.layout.ingredients_listitem, parent, false);
            }
            TextView tv = (TextView)convertView.findViewById(R.id.ingredients_item_nutrient);

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
