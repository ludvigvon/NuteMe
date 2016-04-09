package ca.umontreal.ift2905.nuteme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;

/**
 * Created by h on 09/04/16.
 */
public class MenuMain extends AppCompatActivity {

    private final int REQUEST_CODE = 1;

    ImageView searchButton;
    EditText searchField;
    LinearLayout horizLayout;

    ImageView recipeImg;
    TextView description;

    String query;
    Button addButton;

    View parent;
    int count=0;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        searchButton = (ImageView) findViewById(R.id.searchImageView);
        searchField = (EditText) findViewById(R.id.searchTextField);
        horizLayout = (LinearLayout) findViewById(R.id.horizLinearLayout);
        horizLayout.setTag(R.id.TAG_ID1,count);
        addButton = (Button) findViewById(R.id.addButton);

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
        if(id == R.id.favorites){
            Intent intent = new Intent(this, SearchResults.class);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final Button calcButton = (Button) findViewById(R.id.calcButton);
        if(resultCode == RESULT_OK && requestCode == 1){
            String title = data.getStringExtra("Description");
            //String url = data.getStringExtra("ImgUrl");
            int id = Integer.valueOf(data.getStringExtra("ID"));
            final LinearLayout vertLinearlayout = (LinearLayout) findViewById(R.id.vertLinearLayout);
            LinearLayout ll = (LinearLayout)vertLinearlayout.getChildAt(position);
            ll.setTag(R.id.TAG_ID2,id);
            //EditText et = (EditText) ll.findViewById(R.id.searchTextField);
            //ImageView iv = (ImageView) ll.findViewById(R.id.searchImageView);

            // ViewGroup vg = (ViewGroup) et.getParent();
            //int indexEt = ll.indexOfChild(et);
            //int indexIv = ll.indexOfChild(iv);

            TextView description = new TextView(this);
            ToggleButton fav = new ToggleButton(this);//favorite button
            ImageView img = new ImageView(this);//delete button

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout parent1 = (LinearLayout) v.getParent();
                    LinearLayout parent2 = (LinearLayout) parent1.getParent();
                    int currentIndex = parent2.indexOfChild(parent1);
                    for(int i = currentIndex+1; i < parent2.getChildCount(); ++i){
                        View child = parent2.getChildAt(i);
                        int tag = (int)child.getTag(R.id.TAG_ID1);
                        child.setTag(R.id.TAG_ID1,--tag);
                    }
                    parent2.removeView(parent1);
                    count--;
                    if(count == 0 && (EditText)vertLinearlayout.getChildAt(count).findViewById(R.id.searchTextField)!= null) calcButton.setVisibility(View.INVISIBLE);

                    if(count < 0){
                        count =0;
                        LayoutInflater inflater = getLayoutInflater();

                        //view now is the the linear layout of menu_listitem and we don't need to add the view to relative layout as above the parent of menu_listitem is relative layout
                        View view = inflater.inflate(R.layout.menu_listitem, parent2, false);

                        view.setTag(R.id.TAG_ID1,count);
                        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(La)
                        parent2.addView(view);
                        calcButton.setVisibility(View.INVISIBLE);
                    }

                }
            });

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((ToggleButton) v).isChecked()) {
                        // The toggle is enabled
                        LinearLayout parent1 = (LinearLayout) v.getParent();
                        int id = (Integer)parent1.getTag(R.id.TAG_ID2);
                        Intent intent = new Intent(MenuMain.this, Favorites.class);
                        intent.putExtra("ID", Integer.toString(id));
                        Toast.makeText(MenuMain.this, Integer.toString(id), Toast.LENGTH_SHORT).show();
                    } else {
                        // The toggle is disabled
                    }
                }
            });


            ll.removeAllViews();
            ll.setBackgroundResource(R.drawable.border);
            //LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            //View view = inflater.inflate(R.layout.search_results_checkable_linear_layout, vg, false);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(90,90);
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

            if(calcButton.getVisibility() ==View.INVISIBLE) calcButton.setVisibility(View.VISIBLE);

            //calculate button
            calcButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Integer> ids = new ArrayList<Integer>();
                    int childCount = vertLinearlayout.getChildCount();
                    for(int i=0; i<childCount; ++i){
                        ids.add((Integer) vertLinearlayout.getChildAt(i).getTag(R.id.TAG_ID2));
                        Toast.makeText(MenuMain.this, ""+ids.get(i), Toast.LENGTH_LONG).show();

                    }
                    Intent intent = new Intent(MenuMain.this,DetailedViews.class );
                    intent.putIntegerArrayListExtra("IDS", (ArrayList<Integer>) ids);
                    startActivity(intent);
                }
            });

        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void search(View v){

        View view = (View) v.getParent();//horizontal layout containing search button and textfield

        //int tag = (int) v.getTag();
        EditText text = (EditText)view.findViewById(R.id.searchTextField);
        position = (int)view.getTag(R.id.TAG_ID1);
        //text.setTag(view);
        query = text.getText().toString();
        Intent intent = new Intent(MenuMain.this, SearchResults.class);
        intent.putExtra("Query", query);
        //startActivity(intent);
        startActivityForResult(intent, REQUEST_CODE);

        //return view;
    }

}
