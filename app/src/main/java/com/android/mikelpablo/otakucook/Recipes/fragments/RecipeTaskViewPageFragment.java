package com.android.mikelpablo.otakucook.Recipes.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mikelpablo.otakucook.BroadcastReceiver.AlarmReceiver;
import com.android.mikelpablo.otakucook.Models.Task;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.activities.RecipeTaskViewPageActivity;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    private PendingIntent pi;
    private Intent emptyIntent;
    public static int ID = 1;
    private Task task;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    public int position;

    public RecipeTaskViewPageFragment() {

    }

    public static RecipeTaskViewPageFragment newInstance(Task task,int position) {
        RecipeTaskViewPageFragment fragment = new RecipeTaskViewPageFragment();

        Bundle args = new Bundle();
        args.putParcelable("task", task);
        args.putInt("position",position);
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
        position=getArguments().getInt("position");
        emptyIntent = new Intent();
        task = getArguments().getParcelable("task");
        Log.d("PABJIMCAS","onViewCreated"+task.id);
        if (RecipeTaskViewPageActivity.clicked && task.id == RecipeTaskViewPageActivity.task){
            mBtTimer.setEnabled(false);
            mBtTimer.setBackground(getResources().getDrawable(R.drawable.delete_button));
            mBtTimer.setText(R.string.btalarmWait);
        }
        else if (RecipeTaskViewPageActivity.clicked){
            mBtTimer.setEnabled(false);
            mBtTimer.setBackground(getResources().getDrawable(R.drawable.delete_button));
            mBtTimer.setText(R.string.btalarmWait);
        }else {
            mBtTimer.setEnabled(true);
            mBtTimer.setBackground(getResources().getDrawable(R.drawable.buy_button));
            mBtTimer.setText(R.string.btalarmInit);
        }
        pi = PendingIntent.getActivity(getActivity(), 0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Picasso.with(getContext()).load(task.photo).placeholder(R.drawable.default_recipe).into(mTaskPhoto);
        mTaskDescription.setText(task.description);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        //int notif = RecipeTaskViewPageActivity.manager.getActiveNotifications().length;
        if(task.seconds == 0){
            mBtTimer.setVisibility(View.GONE);
            mCountDown.setVisibility(View.GONE);
        }else {
            long taskMiliseconds = task.seconds * 1000;
            mCountDown.setText(transformTime(taskMiliseconds));
            activateTimer(taskMiliseconds, task);
            mBtTimer.setOnClickListener(this);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("PABJIMCAS","onPause"+task.id);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("PABJIMCAS","onResume"+task.id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("PABJIMCAS","onDestroyView"+task.id);
    }

    private void activateTimer(final long taskMiliseconds, final Task task) {

        countDownTimer = new CountDownTimer(taskMiliseconds,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCountDown.setText(transformTime(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                mCountDown.setText(transformTime(taskMiliseconds));
                mBtTimer.setEnabled(true);
                countDownTimer.cancel();
                RecipeTaskViewPageActivity.task = 0;
                RecipeTaskViewPageActivity.clicked = false;

                FragmentActivity activity = getActivity();
                if (activity != null){
                    positionsChange(position,RecipeTaskViewPageActivity.clicked);
                    mBtTimer.setBackground(getResources().getDrawable(R.drawable.buy_button));
                    mBtTimer.setText(R.string.btalarmInit);
                }


            }
        };
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {

            setRetainInstance(true);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("taskName", task.name);
        intent.putExtra("taskId", (int)task.id);
        intent.putExtra("recipe",RecipeTaskViewPageActivity.recipe.name);

        pendingIntent = PendingIntent.getBroadcast(getContext(), (int) task.id, intent, PendingIntent.FLAG_ONE_SHOT);
        if (v.getId() == R.id.btTimer){

            if(!RecipeTaskViewPageActivity.clicked) {
                Log.d("PABJIMCAS",String.valueOf(position)+" "+task.name);
                mBtTimer.setEnabled(false);

                RecipeTaskViewPageActivity.task = task.id;
                RecipeTaskViewPageActivity.clicked = true;
                positionsChange(position,RecipeTaskViewPageActivity.clicked);
                RecipeTaskViewPageActivity.adapter.notifyDataSetChanged();
                int type = AlarmManager.RTC_WAKEUP;
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, task.seconds);
                long when = calendar.getTimeInMillis();
                
                Log.d("TaskId", String.valueOf(task.id));
                

                countDownTimer.start();
                //mBtTimer.setEnabled(false);
                mBtTimer.setBackground(getResources().getDrawable(R.drawable.delete_button));
                mBtTimer.setText(R.string.btalarmWait);
                alarmManager.setRepeating(type,when,0,pendingIntent);
            } else {
                alarmManager.cancel(pendingIntent);
                RecipeTaskViewPageActivity.clicked = false;

                countDownTimer.cancel();
                long taskMiliseconds = task.seconds * 1000;
                activateTimer(taskMiliseconds, task);
                mCountDown.setText(transformTime(taskMiliseconds));
                mBtTimer.setBackground(getResources().getDrawable(R.drawable.buy_button));
                mBtTimer.setText(R.string.btalarmInit);

            }
        }
    }

    private void positionsChange(int position,boolean clicked) {
        if (position == 0){
           changeButton(position,clicked);

        }
        else if (position == 1){
            changeButton(position,clicked);
            changeButton(position+1,clicked);

        }else if (position == 2){
            changeButton(position-2,clicked);
           changeButton(position+1,clicked);
        }
        else if(position == RecipeTaskViewPageActivity.adapter.getCount()-1){
            changeButton(position-1,clicked);
        }
        else{
            changeButton(position-1,clicked);
            changeButton(position+1,clicked);
        }
    }

    private void changeButton(int position,boolean clicked) {
        RecipeTaskViewPageFragment recipeTaskViewPageFragment= (RecipeTaskViewPageFragment) getActivity().getSupportFragmentManager().getFragments().get(position);
        Log.d("PABJIMCAS","task:"+recipeTaskViewPageFragment.task.name);
        if (clicked){
            recipeTaskViewPageFragment.mBtTimer.setEnabled(false);
            recipeTaskViewPageFragment.mBtTimer.setBackground(getResources().getDrawable(R.drawable.delete_button));
            recipeTaskViewPageFragment.mBtTimer.setText(R.string.btalarmWait);

        }else {
            recipeTaskViewPageFragment.mBtTimer.setEnabled(true);
            recipeTaskViewPageFragment.mBtTimer.setBackground(getResources().getDrawable(R.drawable.buy_button));
            recipeTaskViewPageFragment.mBtTimer.setText(R.string.btalarmInit);
        }

    }

}
