package com.android.mikelpablo.otakucook.Recipes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mikelpablo.otakucook.Main.DrawerItem;
import com.android.mikelpablo.otakucook.Main.MainActivity;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.R;

/**
 * Created by mikelbalducieldiaz on 9/4/16.
 */
public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView name;
    //public Context context;
    public RecipeHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.recipeName);
        itemView.setOnClickListener(this);

    }
    /*public RecipeHolder(Context context,View itemView) {
        super(itemView);
        this.context=context;
        itemView.setOnClickListener(this);
        name = (TextView) itemView.findViewById(R.id.recipeName);
    }*/


    public void bindItem(Recipe recipe) {
        name.setText(recipe.name);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(),name.getText(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(v.getContext(), MainActivity.class);
        v.getContext().startActivity(intent);
    }
}
