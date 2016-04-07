package ca.umontreal.ift2905.nuteme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by h on 26/03/16.
 */
public class RecipeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.recipes_details, container, false);
        ImageView img = (ImageView)v.findViewById(R.id.recipe_image_details);
        TextView title = (TextView)v.findViewById(R.id.recipe_title_details);
        TextView description = (TextView)v.findViewById(R.id.recipe_description);

        Bundle args = getArguments();
        String stitle = args.getString(RecipesPagerFragment.TITLE);
        String surl = args.getString(RecipesPagerFragment.URL);
        //String sdesc =args.getString(RecipesPagerFragment.DESC);

        title.setText(stitle);
        //description.setText(sdesc);
        Picasso.with(getContext()).load(surl).into(img);

        return v;
    }
}
