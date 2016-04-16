package com.android.mikelpablo.otakucook.Ingredients.holders;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Ingredients.Models.Category;
import com.android.mikelpablo.otakucook.Ingredients.activities.IngredientsServerActivity;
import com.android.mikelpablo.otakucook.R;
import com.squareup.picasso.Picasso;

/**
 * Created by pabji on 13/04/2016.
 */
public class CategoriesCollectionHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {

    //@Bind(R.id.category_icon)
    public ImageView category_icon;
    //@Bind(R.id.category_name)
    public TextView category_name;

    public CategoriesCollectionHolder(View itemView) {
        super(itemView);
        category_icon = (ImageView) itemView.findViewById(R.id.category_icon);
        category_name = (TextView) itemView.findViewById(R.id.category_name);
        itemView.setOnClickListener(this);
        //ButterKnife.bind(this, itemView);
    }

    public void bindCategory(Category category){
        category_name.setText(category.name);
        category_icon.setImageDrawable(itemView.getResources().getDrawable(R.mipmap.ic_congelados));
        //Picasso.with(itemView.getContext()).load(R.mipmap.ic_launcher).into(category_icon);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), IngredientsServerActivity.class);
        intent.putExtra("category",category_name.getText());
        v.getContext().startActivity(intent);
    }
}
