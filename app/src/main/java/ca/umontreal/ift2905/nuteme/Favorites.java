package ca.umontreal.ift2905.nuteme;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.umontreal.ift2905.nuteme.DataAccess.DBHelper;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;

public class Favorites extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

//    enum Direction {LEFT, RIGHT;}


//    static final int DELTA = 50;

    public static final String FAVORITE_ID = "id";
    public static final String FAVORITE_JSON = "json";

    ListView lv;
    Button b;

    DBHelper helper;

//    int swipePosition;
//    float historicX = Float.NaN, historicY = Float.NaN;

    List<Recipe> favorites = new ArrayList<Recipe>();
    boolean[] checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);


        lv = (ListView) findViewById(R.id.listView);
        b = (Button) findViewById(R.id.button);
        b.setOnClickListener(Favorites.this);


        helper = new DBHelper(this);
        Cursor cursor = helper.getFavorites();



        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Recipe favorite = new Recipe();
            favorite.id = cursor.getInt(cursor.getColumnIndex(DBHelper.F_ID));
            favorite.title = cursor.getString(cursor.getColumnIndex(DBHelper.F_TITLE));
            favorite.image = cursor.getString(cursor.getColumnIndex(DBHelper.F_IMG_URL));

            favorites.add(favorite);


            cursor.moveToNext();
        }

        checked = new boolean[favorites.size()];
        Arrays.fill(checked, false);


        ListAdapter adapter = new ListAdapter();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(Favorites.this);
//        lv.setOnTouchListener(Favorites.this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        swipePosition = position;
        Log.d("Favorites", "Item clicked id: " + favorites.get(position).id);

//        Intent intent = new Intent(this, DetailsActivity.class);
//        intent.putExtra(FAVORITE_ID, favorites.get(position).id);
//
//        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        List<Recipe> recipes = new ArrayList<Recipe>();
        CheckBox cb;

        for (int i = 0; i < favorites.size(); i++) {
            if (checked[i])
                recipes.add(favorites.get(i));
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Recipe>>() {}.getType();
        String json = gson.toJson(recipes, type);

        Log.d("Json", json);
        List<Recipe> fromJson = gson.fromJson(json, type);

        for (Recipe recipe : fromJson) {
            Log.d("Json", recipe.title);
        }

//        Intent intent = new Intent(this, DetailsActivity.class);
//        intent.putExtra(FAVORITE_JSON, json);
//
//        startActivity(intent);


//        Log.d("Json", json);
//        List<Recipe> fromJson = gson.fromJson(json, type);

//        for (Recipe recipe : fromJson) {
//            Log.d("Json", recipe.title);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int pos = (int) buttonView.getTag();
        checked[pos] = buttonView.isChecked();
        Log.d("onCheckedChanged", String.valueOf(buttonView.isChecked()));
    }

    /*@Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                historicX = event.getX();
                historicY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                if (event.getX() - historicX < -DELTA)
                {
                    FunctionDeleteRowWhenSlidingLeft();
                    return true;
                }
                else if (event.getX() - historicX > DELTA)
                {
                    FunctionDeleteRowWhenSlidingRight();
                    return true;
                } break;
            default: return false;
        }
        return false;
    }

    private void FunctionDeleteRowWhenSlidingLeft() {
        FunctionDeleteRowWhenSlidingRight();
    }

    private void FunctionDeleteRowWhenSlidingRight() {

    }*/

    public class ListAdapter extends BaseAdapter {

        LayoutInflater inflater;

        public ListAdapter() {
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return favorites.size();
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
                v = inflater.inflate(R.layout.favorites_rows, parent, false);
            }

            CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox);
            cb.setTag(position);

            cb.setOnCheckedChangeListener(Favorites.this);

            TextView tv = (TextView) v.findViewById(R.id.row_text);
            ImageView iv = (ImageView) v.findViewById(R.id.row_image);

            Recipe recipe = favorites.get(position);
            String title = favorites.get(position).title;
            String url = favorites.get(position).image;

            v.setTag(recipe);
            tv.setText(title);
            Picasso.with(getApplicationContext()).load(url).into(iv);

//            if (position % 2 == 0) {
//                v.setBackgroundColor(0xFF00FF00);
//            } else {
//                v.setBackgroundColor(0xFFFFFFFF);
//            }

            return v;
        }
    }


    /*public class APITask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //Toast.makeText(Favorites.this, "new favorites: ", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            APIHelper api = new APIHelper();
            try {
                int[] ids = new int[]{310658, 163336, 203834, 325816, 485365};
                Recipe recipe;

                for (int i = 0; i < ids.length; i++) {
                    try {
                        recipe = api.getRecipe(ids[i]);
                        helper.insertFavorite(recipe);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }catch (Exception e){
                Log.d("doInBackground", e.getMessage());
            }
            return null;
        }
    }*/

}
