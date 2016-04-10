package ca.umontreal.ift2905.nuteme;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import ca.umontreal.ift2905.nuteme.BusinessLogic.DataAggregator;
import ca.umontreal.ift2905.nuteme.DataAccess.APIHelper;
import ca.umontreal.ift2905.nuteme.DataModel.Nutrition;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;

public class Summary extends AppCompatActivity implements View.OnClickListener {

    Button getDetailedViews;

    List<Recipe> recipes;
    Nutrition nutritionSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);

        Intent intent = getIntent();
        List<Integer> ids = intent.getIntegerArrayListExtra(MenuMain.MENU_IDS);
        ApiAsyncTask run = new ApiAsyncTask(ids);
        run.execute();

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
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, DetailedViews.class);
        startActivity(intent);
    }




}
