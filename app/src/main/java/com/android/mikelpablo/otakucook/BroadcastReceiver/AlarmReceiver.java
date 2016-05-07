package com.android.mikelpablo.otakucook.BroadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.android.mikelpablo.otakucook.Login.activities.LoginActivity;
import com.android.mikelpablo.otakucook.Main.activities.MainActivity;
import com.android.mikelpablo.otakucook.R;
import com.android.mikelpablo.otakucook.Recipes.fragments.RecipeTaskViewPageFragment;

public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        Intent emptyIntent = new Intent(context, LoginActivity.class);
        String taskName = intent.getStringExtra("taskName");
        String recipeName = intent.getStringExtra("recipe");
        int taskId = intent.getIntExtra("taskId",0);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,emptyIntent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(recipeName)
                .setContentText(String.format(context.getString(R.string.title_alarm_receiver), taskName))
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true).build();

        notificationManager.notify(taskId,notification);
    }
}
