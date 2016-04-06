package com.android.mikelpablo.otakucook.Main;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.mikelpablo.otakucook.R;

import java.util.ArrayList;

/**
 * Created by pabji on 05/04/2016.
 */
public class DrawerMenu extends LinearLayout {

    private RecyclerView drawerList;
    private Context context;

    public DrawerMenu(Context context) {
        super(context);
        this.context = context;
    }

    public DrawerMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(VERTICAL);

        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        setBackgroundColor(typedValue.data);

        LayoutInflater.from(context).inflate(R.layout.view_drawer_menu, this, true);
    }

    public void setList(String[] menuItems) {
        drawerList = (RecyclerView) findViewById(R.id.list_view);

        ArrayList<DrawerItem> items = new ArrayList<DrawerItem>();
        items.add(new DrawerItem(menuItems[0]));
        items.add(new DrawerItem(menuItems[1]));
        items.add(new DrawerItem(menuItems[2]));
        items.add(new DrawerItem(menuItems[3]));

        drawerList.setAdapter(new DrawerListAdapter(items));
        drawerList.setLayoutManager(new LinearLayoutManager(context));
    }
}
