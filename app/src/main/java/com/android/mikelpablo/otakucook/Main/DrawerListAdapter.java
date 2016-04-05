package com.android.mikelpablo.otakucook.Main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.R;

import java.util.List;

/**
 * Created by pabji on 04/04/2016.
 */
public class DrawerListAdapter extends ArrayAdapter {

    public DrawerListAdapter(Context context, List objects) {
        super(context, 0,objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_menu_item,null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.name);

        DrawerItem item = (DrawerItem) getItem(position);
        name.setText(item.getName());

        return convertView;
    }
}
