package com.android.mikelpablo.otakucook.Main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.R;
import com.squareup.picasso.Picasso;

/**
 * Created by pabji on 05/04/2016.
 */
public class DrawerItemHolder extends RecyclerView.ViewHolder {
    private ImageView icon;
    private TextView name;
    public DrawerItemHolder(View itemView) {
        super(itemView);

        icon = (ImageView)itemView.findViewById(R.id.item_icon);
        name = (TextView) itemView.findViewById(R.id.item_name);
    }

    public void bindItem(DrawerItem drawerItem) {
        name.setText(drawerItem.getName());
    }
}
