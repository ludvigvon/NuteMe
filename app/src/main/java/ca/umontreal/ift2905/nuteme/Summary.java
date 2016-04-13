package ca.umontreal.ift2905.nuteme;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ca.umontreal.ift2905.nuteme.BusinessLogic.DataAggregator;
import ca.umontreal.ift2905.nuteme.DataAccess.APIHelper;
import ca.umontreal.ift2905.nuteme.DataModel.Nutrient;
import ca.umontreal.ift2905.nuteme.DataModel.Nutrition;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;
import ca.umontreal.ift2905.nuteme.DataModel.SimpleRecipe;

public class Summary extends AppCompatActivity implements View.OnClickListener {

    ListView lv;
    Button getDetailedViews;

    List<Recipe> recipes;
    List<Nutrient> nutritionSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);

        Intent intent = getIntent();
        List<Integer> ids = intent.getIntegerArrayListExtra(MenuMain.MENU_IDS);
        ApiAsyncTask run = new ApiAsyncTask(ids);
        run.execute();

        lv = (ListView)findViewById(R.id.summary_listView);
        getDetailedViews = (Button)findViewById(R.id.summary_button_detailedViews);
        getDetailedViews.setOnClickListener(this);
    }

    public class ApiAsyncTask extends AsyncTask<String, String, List<Recipe>> {

        List<Integer> ids;

        public ApiAsyncTask(List<Integer> ids) {
            this.ids = ids;
        }

        @Override
        protected List<Recipe> doInBackground(String... params) {
            APIHelper api = APIHelper.getInstance();
            try {
                recipes = api.getDetailedRecipes(ids);
            }catch (Exception e){
                Log.d("doInBackground", e.getMessage());
            }
            return recipes;
        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            super.onPostExecute(recipes);

            nutritionSummary = new DataAggregator().getSummaryAggregateView(recipes);

            ListAdapter adapter = new ListAdapter();
            lv.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, DetailedViews.class);
        startActivity(intent);
    }

    public class ListAdapter extends BaseAdapter {

        LayoutInflater inflater;

        public ListAdapter() {
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return nutritionSummary.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = inflater.inflate(R.layout.summary_listitem, parent, false);
            }


            TextView title = (TextView) v.findViewById(R.id.summary_listitem_nutrient);
            TextView amount = (TextView) v.findViewById(R.id.summary_listitem_qty);

            Nutrient nutrient = nutritionSummary.get(position);

            title.setText(nutrient.title);
            amount.setText(nutrient.amount + nutrient.unit);

            return v;
        }
    }
}
