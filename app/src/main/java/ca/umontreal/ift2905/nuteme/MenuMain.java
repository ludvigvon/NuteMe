package ca.umontreal.ift2905.nuteme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ca.umontreal.ift2905.nuteme.DataAccess.DBHelper;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;
import ca.umontreal.ift2905.nuteme.DataModel.SimpleRecipe;

/**
 * Created by h on 09/04/16.
 */
public class MenuMain extends AppCompatActivity {

    public static final String MENU_IDS = "MenuIDs";
    private final int SEARCH_RESULTS_REQUEST_CODE = 1;
    private final int FAVORITES_REQUEST_CODE = 2;

    ImageView searchButton;
    EditText searchField;
    LinearLayout horizLayout;

    ImageView recipeImg;
    TextView description;

    String query;
    Button addButton;
    Button removeButton;

    View parent;
    int count = 0;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        searchButton = (ImageView) findViewById(R.id.searchImageView);
        searchField = (EditText) findViewById(R.id.searchTextField);
        horizLayout = (LinearLayout) findViewById(R.id.horizLinearLayout);
        horizLayout.setTag(R.id.TAG_ID1, count);
        addButton = (Button) findViewById(R.id.addButton);
        removeButton =(Button) findViewById(R.id.removeButton);

        //menu_listitem button clickListener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(v);
            }
        });

        //add (+) button clickListener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                //inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                LinearLayout vertLinearlayout = (LinearLayout) findViewById(R.id.vertLinearLayout);//parent
                //View view = inflater.inflate(R.layout.menu_listitem, null);

                //view now is the the linear layout of menu_listitem and we don't need to add the view to relative layout as above the parent of menu_listitem is relative layout
                View view = inflater.inflate(R.layout.menu_listitem, vertLinearlayout, false);

                //view.findViewById(R.id.searchTextField).setTag(count++);
                //view.findViewById(R.id.searchImageView).setTag(count++);
                view.setTag(R.id.TAG_ID1, ++count);
                //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(La)
                vertLinearlayout.addView(view);
                //--------------->added removeButton
                //remove button (-)
                removeButton = (Button)view.findViewById(R.id.removeButton);
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout vertLinearlayout = (LinearLayout) findViewById(R.id.vertLinearLayout);//parent
                        LinearLayout horizLinearlayout = (LinearLayout) v.getParent();

                        int currentIndex = vertLinearlayout.indexOfChild(horizLinearlayout);
                        for (int i = currentIndex + 1; i < vertLinearlayout.getChildCount(); ++i) {
                            View child = vertLinearlayout.getChildAt(i);
                            int tag = (int) child.getTag(R.id.TAG_ID1);
                            child.setTag(R.id.TAG_ID1, --tag);
                        }

                        count--;
                        vertLinearlayout.removeView(horizLinearlayout);
                    }
                });
            }
        });
    }//End OnCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.favorites_star) {

            Intent intent = new Intent(MenuMain.this, Favorites.class);
            startActivityForResult(intent, FAVORITES_REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final Button calcButton = (Button) findViewById(R.id.calcButton);
        final LinearLayout vertLinearlayout = (LinearLayout) findViewById(R.id.vertLinearLayout);
        if (resultCode == RESULT_OK && requestCode == SEARCH_RESULTS_REQUEST_CODE) {
            String title = data.getStringExtra("Description");
            String url = data.getStringExtra("ImgUrl");
            int id = Integer.valueOf(data.getStringExtra("ID"));
            //layout containing the search and text field
            LinearLayout ll = (LinearLayout) vertLinearlayout.getChildAt(position);
            ll.setTag(R.id.TAG_ID2, id);
            ll.setTag(R.id.TAG_ID3, title);
            ll.setTag(R.id.TAG_ID4, url);
            addItems(calcButton, vertLinearlayout, ll, title);

        } else if (resultCode == RESULT_OK && requestCode == FAVORITES_REQUEST_CODE) {
            // TODO: Mina, voici le data provenant de Favorites. Peux-tu t'occuper de l'affichage?
            Gson gson = new Gson();
            Type type = new TypeToken<List<Recipe>>() {
            }.getType();
            String json = data.getStringExtra(Favorites.FAVORITE_JSON);
            List<SimpleRecipe> recipes = gson.fromJson(json, type);

            for (SimpleRecipe recipe : recipes) {
                //Log.d("Json", recipe.title);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin = 20;

                LinearLayout ll = new LinearLayout(this);
                ll.setLayoutParams(layoutParams);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setTag(R.id.TAG_ID2, recipe.id);
                ll.setTag(R.id.TAG_ID3, recipe.title);
                ll.setTag(R.id.TAG_ID4, recipe.image);
                vertLinearlayout.addView(ll);
                count++;
                addItems(calcButton, vertLinearlayout, ll, recipe.title);
            }
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void search(View v) {

        View view = (View) v.getParent();//horizontal layout containing search button and textfield

        //int tag = (int) v.getTag();
        EditText text = (EditText) view.findViewById(R.id.searchTextField);
        position = (int) view.getTag(R.id.TAG_ID1);
        //text.setTag(view);
        query = text.getText().toString();
        Intent intent = new Intent(MenuMain.this, SearchResults.class);
        intent.putExtra("Query", query);
        //startActivity(intent);
        startActivityForResult(intent, SEARCH_RESULTS_REQUEST_CODE);

        //return view;
    }


    //add new items to the main activity

    public void addItems(final Button calcButton, final LinearLayout vertLinearlayout, final LinearLayout ll, String title){


        //result view delete button, text, fav icon
        TextView description = new TextView(this);
        ToggleButton fav = new ToggleButton(this);//favorite button
        ImageView img = new ImageView(this);//delete button


        //listener for the delete button
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout parent1 = (LinearLayout) v.getParent();
                LinearLayout parent2 = (LinearLayout) parent1.getParent();
                int currentIndex = parent2.indexOfChild(parent1);
                for (int i = currentIndex + 1; i < parent2.getChildCount(); ++i) {
                    View child = parent2.getChildAt(i);
                    int tag = (int) child.getTag(R.id.TAG_ID1);
                    child.setTag(R.id.TAG_ID1, --tag);
                }
                parent2.removeView(parent1);
                count--;
                if (count == 0 && vertLinearlayout.getChildAt(count).findViewById(R.id.searchTextField) != null) {
                    calcButton.setVisibility(View.INVISIBLE);
                }
                else {
                    int nbChilds = parent2.getChildCount();
                    int j = 0;
                    for (int i = 0; i < nbChilds; ++i) {
                        if ((EditText) parent2.getChildAt(i).findViewById(R.id.searchTextField) != null)
                            ++j;
                    }
                    if (j == nbChilds) calcButton.setVisibility(View.INVISIBLE);
                }
                int k = 0;
                int nbChilds = parent2.getChildCount();
                for (int i = 0; i < nbChilds; ++i) {
                    if ((Button) parent2.getChildAt(i).findViewById(R.id.removeButton) != null)
                        ++k;
                }
                if (k == nbChilds){
                    count++;
                    LayoutInflater inflater = getLayoutInflater();

                    //view now is the the linear layout of menu_listitem and we don't need to add the view to relative layout as above the parent of menu_listitem is relative layout
                    View view = inflater.inflate(R.layout.menu_listitem_noclear, parent2, false);

                    view.setTag(R.id.TAG_ID1, 0);

                    for (int i = 0; i < parent2.getChildCount(); ++i) {
                        View child = parent2.getChildAt(i);
                        int tag = (int) child.getTag(R.id.TAG_ID1);
                        child.setTag(R.id.TAG_ID1, ++tag);
                    }
                    //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(La)
                    parent2.addView(view,0);


                }

                if (count < 0) {
                    count = 0;
                    LayoutInflater inflater = getLayoutInflater();

                    //view now is the the linear layout of menu_listitem and we don't need to add the view to relative layout as above the parent of menu_listitem is relative layout
                    View view = inflater.inflate(R.layout.menu_listitem_noclear, parent2, false);//------------->added noclear

                    view.setTag(R.id.TAG_ID1, count);
                    //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(La)
                    parent2.addView(view);
                    calcButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        //listener for the fav button
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBHelper helper = new DBHelper(v.getContext());
                SimpleRecipe recipe = new SimpleRecipe();
                LinearLayout parent1 = (LinearLayout) v.getParent();
                int id = (Integer) parent1.getTag(R.id.TAG_ID2);
                String title = (String) parent1.getTag(R.id.TAG_ID3);
                String url = (String) parent1.getTag(R.id.TAG_ID4);
                if (((ToggleButton) v).isChecked()) {
                    // The toggle is enabled

                    //Intent intent = new Intent(MenuMain.this, Favorites.class);
                    //intent.putExtra("ID", Integer.toString(id));
                    //Toast.makeText(MenuMain.this, Integer.toString(id), Toast.LENGTH_SHORT).show();

                    // TODO: Mina, quand checked, il faut récupérer les données de la recette et faire DBHelper.insertFavorite(recipe)

                    recipe.id = id;
                    recipe.title = title;
                    recipe.image = url;
//                        test.id = 156992; test.title = "Char-Grilled Beef Tenderloin with Three-Herb Chimichurri";
//                        test.readyInMinutes = 45; test.image = "https://spoonacular.com/recipeImages/char-grilled-beef-tenderloin-with-three-herb-chimichurri-156992.jpg";
                    helper.insertFavorite(recipe);


                } else {
                    // The toggle is disabled
                    // TODO: Mina, quand unchecked, il faut récupérer le id de la recette et faire DBHelper.deleteByID(recipeId):
                    //test.id = 156992;
                    recipe.id = id;
                    helper.deleteByID(recipe.id);
                }
            }
        });

        ll.removeAllViews();//remove search and text field
        ll.setBackgroundResource(R.drawable.border);
        //LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //View view = inflater.inflate(R.layout.search_results_checkable_linear_layout, vg, false);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(90, 90);
        layoutParams1.weight = 1;
        layoutParams2.gravity = Gravity.CENTER_VERTICAL;
        description.setLayoutParams(layoutParams1);
        img.setLayoutParams(layoutParams2);

        fav.setLayoutParams(layoutParams2);

        fav.setBackgroundResource(R.drawable.toggle_button);
        fav.setPadding(20, 20, 20, 20);
        fav.setChecked(false);
        fav.setTextOff("");
        fav.setTextOn("");

        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        img.setPadding(20, 20, 20, 20);
        img.setImageResource(R.drawable.ic_close);
        description.setText(title);
        //description.setGravity(Gravity.CENTER);
        description.setTextColor(Color.WHITE);
        description.setPadding(20, 20, 20, 20);
        description.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        //Picasso.with(getApplicationContext()).load(url).into(img);

        ll.addView(img);
        ll.addView(description);
        ll.addView(fav);

        if (calcButton.getVisibility() == View.INVISIBLE)
            calcButton.setVisibility(View.VISIBLE);

        //calculate button
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> ids = new ArrayList<>();
                int childCount = vertLinearlayout.getChildCount();
                for (int i = 0; i < childCount; ++i) {
                    ids.add((Integer) vertLinearlayout.getChildAt(i).getTag(R.id.TAG_ID2));
                }
                Intent intent = new Intent(MenuMain.this, Summary.class);
                intent.putIntegerArrayListExtra(MENU_IDS, ids);
                startActivity(intent);
            }
        });
    }

}