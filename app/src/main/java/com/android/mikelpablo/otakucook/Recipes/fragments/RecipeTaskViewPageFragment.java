package com.android.mikelpablo.otakucook.Recipes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mikelpablo.otakucook.Models.Task;
import com.android.mikelpablo.otakucook.R;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mikelbalducieldiaz on 12/4/16.
 */
public class RecipeTaskViewPageFragment extends Fragment {
    @Bind(R.id.taskPhoto)
    ImageView mTaskPhoto;
    @Bind(R.id.taskDescription)
    TextView mTaskDescription;
    public RecipeTaskViewPageFragment() {
    }

    public static RecipeTaskViewPageFragment newInstance(Task task) {
        RecipeTaskViewPageFragment fragment = new RecipeTaskViewPageFragment();
        Bundle args = new Bundle();
        args.putParcelable("task", task);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_task_view_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        Task task = getArguments().getParcelable("task");
        Picasso.with(getContext()).load(task.photo).into(mTaskPhoto);
        mTaskDescription.setText(task.description);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {


        }

    }
}
