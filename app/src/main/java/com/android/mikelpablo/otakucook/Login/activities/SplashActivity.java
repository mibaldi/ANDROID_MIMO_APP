package com.android.mikelpablo.otakucook.Login.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.mikelpablo.otakucook.Models.Recipe;
import com.android.mikelpablo.otakucook.MyApiClient.MyAPI;
import com.android.mikelpablo.otakucook.MyApiClient.MyApiClient;
import com.android.mikelpablo.otakucook.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    public static final int seconds = 4;
    public static final int delay = 1;
    public static  final int miliseconds= seconds * 1000;
    private MyAPI service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Picasso.with(this).load(R.drawable.titulo_app).into(imageView);
        progressBar.setMax(max_progress());
        serverWakeUp();
        startAnimation();
    }

    private void serverWakeUp() {
        service = MyApiClient.createService(MyAPI.class);
        Call<List<Recipe>> recipes = service.recipes();
        recipes.enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
    }

    private int max_progress() {
        return seconds-delay;
    }

    public void startAnimation(){
        new CountDownTimer(miliseconds,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(set_progress(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }


    private int set_progress(long milisegundos) {
        return (int)((miliseconds-milisegundos)/1000);
    }

}
