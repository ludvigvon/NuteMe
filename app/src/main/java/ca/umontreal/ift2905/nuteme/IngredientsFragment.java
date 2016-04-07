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

import ca.umontreal.ift2905.nuteme.DataModel.Recipe;

/**
 * Created by h on 26/03/16.
 */
public class IngredientsFragment extends Fragment {

    ExpandableListView listView;
    List<Recipe> recipes;
    DetailedViews parentActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentActivity = (DetailedViews) getActivity();

        View v = inflater.inflate(R.layout.ingredients_details, container, false);
        listView = (ExpandableListView)v.findViewById(R.id.ingredients_expandableListView);

//        Bundle args = getArguments();
//        String json = args.getString(DetailedViews.RECIPES);
//
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<Recipe>>() {}.getType();
//        recipes = gson.fromJson(json, type);

        recipes = parentActivity.getRecipes();

        for (Recipe recipe : recipes) {
            Log.d("Json", recipe.title);
        }

        ListAdapter adapter = new ListAdapter();
        listView.setAdapter(adapter);

        return v;
    }

    public class ListAdapter extends BaseExpandableListAdapter {

        //Context ctx;
        LayoutInflater inflater;

        Recipe testRecipe = recipes.get(0);

        public ListAdapter(){
            //this.ctx = context;
            inflater = (LayoutInflater)parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getGroupCount() {
            // TODO: aggregate ingredients over all recipes
            return testRecipe.nutrition.ingredients.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            // TODO: aggregate ingredients over all recipes
            return testRecipe.nutrition.ingredients.get(groupPosition).nutrients.size();
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
            tv.setText(testRecipe.nutrition.ingredients.get(groupPosition).name);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = inflater.inflate(R.layout.ingredients_listitem, parent, false);
            }
            TextView tv = (TextView)convertView.findViewById(R.id.ingredients_item_nutrient);

            String name = testRecipe.nutrition.ingredients.get(groupPosition).nutrients.get(childPosition).name;

            tv.setText(name);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

}
