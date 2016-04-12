package com.android.mikelpablo.otakucook.Main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.R;
import com.google.android.gms.common.SignInButton;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pabji on 05/04/2016.
 */
public class DrawerMenu extends LinearLayout {

    private TextView m_selectedItem;

    private Context context;
    @Bind(R.id.login_with_google)
    SignInButton mGoogleLoginButton;

    public interface Listener {
        void onItemClick(int itemId);
    }

    public DrawerMenu(Context context) {
        super(context);
        this.context = context;
    }

    public DrawerMenu(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(VERTICAL);

        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        setBackgroundColor(typedValue.data);

        LayoutInflater.from(context).inflate(R.layout.view_drawer_menu, this, true);
        ButterKnife.bind(this);

        mGoogleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) context;
                if (!activity.mGoogleApiClient.isConnecting()) {
                    activity.connectToApiClient();
                }
            }
        });
    }

    @OnClick({R.id.main, R.id.shopping_cart, R.id.recipes, R.id.ingredients})
    void onItemClick(View view) {
        if (getContext() instanceof Listener) {
            ((Listener) getContext()).onItemClick(view.getId());
        }
    }

    public void setSelectedItemId(int itemId) {
        /*if (m_selectedItem != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                m_selectedItem.getCompoundDrawables()[0].setTintList(null);
            m_selectedItem.setTypeface(null, Typeface.NORMAL);
        }*/
        if (m_selectedItem != null) {
            m_selectedItem.setTypeface(null, Typeface.NORMAL);
        }
        TextView view = (TextView) findViewById(itemId);

        // Ponemos en negrita el texto del elemento seleccionado para resaltarlo. Otra opción
        // hubiera sido activarlo (view.setActivated(true)) pero para que eso tenga algún efecto
        // también habría que ponerle al item como background un selector que tuviera un color
        // diferente para el estado "activated" (el background actual es "selectableItemBackground"
        // que no cumple esta condición)
        view.setTypeface(null, Typeface.BOLD);

        m_selectedItem = view;
    }

    public int getSelectedItemId() {
        return m_selectedItem.getId();
    }
}


