package ca.umontreal.ift2905.nuteme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import ca.umontreal.ift2905.nuteme.DataAccess.APIHelper;
import ca.umontreal.ift2905.nuteme.DataModel.Recipes;
import ca.umontreal.ift2905.nuteme.DataModel.SimpleRecipe;

/**
 * Created by h on 09/04/16.
 */
public class SearchResults extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView list;
    Button okButton;
    String query;
    MyAdapter adapter;
    SimpleRecipe recipe;
    Recipes recipes;
    TextView title;
    RadioButton radio;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_activity);
        title = (TextView) findViewById(R.id.titleLabel);
        list = (ListView) findViewById(R.id.listView);
        //radio = (RadioButton) findViewById(R.id.radioButton);
        okButton = (Button) findViewById(R.id.OkButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position;
                for(int i = 0; i<list.getChildCount(); ++i){
                    radio = (RadioButton)list.getChildAt(i).findViewById(R.id.radioButton);
                    if(radio.isChecked()){
                        position = (Integer)radio.getTag();

                        //int position = list.getCheckedItemPosition();
                        if(position > -1){
                            //Toast.makeText(SearchResultActivity.this, Integer.toString(position), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SearchResults.this, MenuMain.class);
                    /*
                    View view = list.getChildAt(position - list.getFirstVisiblePosition());
                    TextView tv = (TextView)view.findViewById(R.id.textView);
                    ImageView iv = (ImageView)view.findViewById(R.id.imageView);
                    intent.putExtra("Description",tv.getText());
                    String title = root.results.get(position).title;
                    */
                            //Toast.makeText(SearchResultActivity.this, tv.getText(), Toast.LENGTH_SHORT).show();
                            String title = recipes.results.get(position).title;
                            intent.putExtra("Description",title);
                            String imgUrl = recipes.baseUri + recipes.results.get(position).image;
                            intent.putExtra("ImgUrl", imgUrl);
                            int id = recipes.results.get(position).id;
                            intent.putExtra("ID",Integer.toString(id));
                            //Toast.makeText(SearchResultActivity.this,title, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(SearchResultActivity.this,imgUrl, Toast.LENGTH_SHORT).show();

                            setResult(RESULT_OK, intent);
                            finish();
                    /*adapter.ge
                    Intent intent = new Intent(SearchResultActivity.this, MainActivity.class);
                    intent.putExtra("descriptionText", );
                    intent.putExtra("url", );*/
                        }

                        break;
                    }
                    /*
                    else{
                        Toast.makeText(SearchResults.this, "Select an item", Toast.LENGTH_SHORT).show();
                    }
                    */

                }

            }
        });
        Intent intent = getIntent();
        query = intent.getStringExtra("Query");
        title.setText("Search Results for " + query);
        RunAPI run = new RunAPI();
        run.execute();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//        if(id == R.id.favorites_star){
//            Intent intent = new Intent(this, MenuMain.class);
//            startActivity(intent);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: Mina - Il ne faut pas que la description de la recette s'ouvre
        // lorsqu'on ne fait que sélectionner le radio button
        Log.d("Favorites", "Item clicked id: " + recipes.results.get(position).id);


            Intent intent = new Intent(this, RecipeDescription.class);
            intent.putExtra(getString(R.string.RECIPE_ID), recipes.results.get(position).id);

            startActivity(intent);

    }


    public class MyAdapter extends BaseAdapter {

        LayoutInflater inflater;
        int selectedPosition = 0;


        public MyAdapter() {
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return recipes.totalResults;
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
            ViewHolder holder;

            if (v == null) {
                v = inflater.inflate(R.layout.search_results_listitem, parent, false);
                holder = new ViewHolder();
                holder.imgView = (ImageView)v.findViewById(R.id.imageView);
                holder.recipeTitle = (TextView) v.findViewById(R.id.textView);
                holder.radioButton = (RadioButton) v.findViewById(R.id.radioButton);


                v.setTag(holder);
                //RadioButton radioButton = (RadioButton) v.findViewById(R.id.radioButton);


            }
            else{
                holder = (ViewHolder)v.getTag();
            }

            holder.radioButton.setChecked(position == selectedPosition);
            holder.radioButton.setTag(position);

            holder.radioButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    selectedPosition = (Integer) v.getTag();
                    notifyDataSetChanged();
                }
            });


            //if we don't use ViewHolder
            //TextView recipeTitle = (TextView) v.findViewById(R.id.textView);
            //ImageView img = (ImageView) v.findViewById(R.id.imageView);


            String title = recipes.results.get(position).title;

            String imgUrl = recipes.baseUri + recipes.results.get(position).image;

            holder.recipeTitle.setText(title);
            Picasso.with(getApplicationContext()).load(imgUrl).into(holder.imgView);


            return v;
        }

        //use ViewHolder to increase performance
        private class ViewHolder{

            ImageView imgView;
            TextView recipeTitle;
            RadioButton radioButton;

        }
    }

    public class RunAPI extends AsyncTask<String, Object, Recipes> {


        @Override
        protected Recipes doInBackground(String... params) {

            APIHelper api = APIHelper.getInstance();
            try {
                recipes = api.seachRecipes(query);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(Recipes result) {

            super.onPostExecute(result);
            adapter = new MyAdapter();
            list.setAdapter(adapter);
            list.setOnItemClickListener(SearchResults.this);//to be completed
        }
    }


}
