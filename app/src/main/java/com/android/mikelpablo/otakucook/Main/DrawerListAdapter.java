package com.android.mikelpablo.otakucook.Main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pabji on 04/04/2016.
 */
public class DrawerListAdapter extends RecyclerView.Adapter<DrawerItemHolder> {

    private List<DrawerItem> listItem;


    public DrawerListAdapter(List<DrawerItem> objects) {
        listItem = objects;
    }

    @Override
    public DrawerItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item,parent,false);
        return new DrawerItemHolder(view);
    }

    @Override
    public void onBindViewHolder(DrawerItemHolder holder, int position) {
        holder.bindItem(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
}
