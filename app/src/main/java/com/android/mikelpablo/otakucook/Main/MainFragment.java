package com.android.mikelpablo.otakucook.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mikelpablo.otakucook.R;

/**
 * Created by pabji on 04/04/2016.
 */
public class MainFragment extends Fragment {

    public MainFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);
        String title = getResources().getStringArray(R.array.mainMenu)[0];
        getActivity().setTitle(title);
        return rootView;
    }
}
