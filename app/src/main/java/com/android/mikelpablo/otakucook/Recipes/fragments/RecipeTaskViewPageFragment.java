package com.android.mikelpablo.otakucook.Recipes.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mikelpablo.otakucook.Models.Task;
import com.android.mikelpablo.otakucook.R;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mikelbalducieldiaz on 12/4/16.
 */
public class RecipeTaskViewPageFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.taskPhoto)
    ImageView mTaskPhoto;
    @Bind(R.id.taskDescription)
    TextView mTaskDescription;
    @Bind(R.id.countdown)
    TextView mCountDown;
    @Bind(R.id.btTimer)
    Button mBtTimer;
    private CountDownTimer countDownTimer;

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

    public String transformTime (long milliseconds){
        int seconds = (int) milliseconds/1000;
        String secondsString = String.valueOf(seconds%60);
        String minutesString = "00";
        String hoursString = "00";
        if (secondsString.length() < 2){
            secondsString = '0'+secondsString;
        }
        if(seconds > 59){
            int minutes = seconds/60;
            minutesString = String.valueOf(minutes%60);
            if(minutesString.length() < 2){
                minutesString = '0'+minutesString;
            }
            if(minutes > 59){
                int hours = minutes/60;
                hoursString = String.valueOf(hours%24);
                if(hoursString.length() < 2){
                    hoursString = '0'+hoursString;
                }
            }
        }
        return hoursString+":"+minutesString+":"+secondsString;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        final Task task = getArguments().getParcelable("task");
        Picasso.with(getContext()).load(task.photo).into(mTaskPhoto);
        mTaskDescription.setText(task.description);
        final long taskMiliseconds = task.seconds*1000;
        mCountDown.setText(transformTime(taskMiliseconds));
        activateTimer(taskMiliseconds,task);
        mBtTimer.setOnClickListener(this);


    }

    private void activateTimer(final long taskMiliseconds,final Task task) {
        countDownTimer = new CountDownTimer(taskMiliseconds,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCountDown.setText(transformTime(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                mCountDown.setText(transformTime(taskMiliseconds));
                mBtTimer.setEnabled(true);
                mBtTimer.setText("Start");
                Toast.makeText(getContext(),"Times'up! Task "+task.name+ " has finished", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {


        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btTimer){
            countDownTimer.start();
            mBtTimer.setEnabled(false);
            mBtTimer.setText("Wait!");
        }
    }
}
