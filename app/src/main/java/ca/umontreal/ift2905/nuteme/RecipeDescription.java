package ca.umontreal.ift2905.nuteme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipeDescription extends AppCompatActivity {

    private int recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_description_activity);

//        Intent intent = getIntent();
//        recipeId = intent.getIntExtra(getString(R.string.RECIPE_ID), 0);
    }

    public int getRecipeId(){
        return recipeId;
    }

}
