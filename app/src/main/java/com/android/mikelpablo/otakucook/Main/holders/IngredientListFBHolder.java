package com.android.mikelpablo.otakucook.Main.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Main.activities.MainActivity;
import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;
import com.firebase.client.Firebase;

import butterknife.Bind;
import retrofit2.Call;

public class IngredientListFBHolder extends RecyclerView.ViewHolder{
    public TextView name;
    public Button delete;
    public Button add;
    public ImageView icon;
    public String id;
    public Context context;

    public IngredientListFBHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        name = (TextView) itemView.findViewById(R.id.ingredientName);
        delete = (Button) itemView.findViewById(R.id.btEliminarIngrediente);
        add = (Button) itemView.findViewById(R.id.btAddIngredient);
    }

}
