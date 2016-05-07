package com.android.mikelpablo.otakucook.Ingredients.holders;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Ingredients.Models.Category;
import com.android.mikelpablo.otakucook.Ingredients.activities.IngredientsServerActivity;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Utils.Connectivity;
import com.squareup.picasso.Picasso;

/**
 * Created by pabji on 13/04/2016.
 */
public class CategoriesCollectionHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView category_icon;
    public TextView category_name;

    public CategoriesCollectionHolder(View itemView) {
        super(itemView);
        category_icon = (ImageView) itemView.findViewById(R.id.category_icon);
        category_name = (TextView) itemView.findViewById(R.id.category_name);
        itemView.setOnClickListener(this);
    }

    public void bindCategory(Category category){
        category_name.setText(category.name);
        category_icon.setImageDrawable(category.icon_id);
    }

    @Override
    public void onClick(View v) {
        if(Connectivity.isNetworkAvailable(v.getContext())){
            Intent intent = new Intent(v.getContext(), IngredientsServerActivity.class);
            intent.putExtra("category",category_name.getText());
            v.getContext().startActivity(intent);
        }else{
            Snackbar.make(v, R.string.no_connectivity, Snackbar.LENGTH_LONG).show();
        }
    }
}
