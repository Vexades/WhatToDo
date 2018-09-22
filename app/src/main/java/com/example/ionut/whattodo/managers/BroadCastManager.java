package com.example.ionut.whattodo.managers;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.ionut.whattodo.MainScreen;
import com.example.ionut.whattodo.R;
import com.example.ionut.whattodo.database.ToDoDatabase;


 public class BroadCastManager extends BroadcastReceiver {

     @SuppressWarnings("deprecation")
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("toDoName");
        int getId = intent.getIntExtra("id", 0);
        long x = intent.getLongExtra("test", 0);
         boolean paused = intent.getBooleanExtra("paused", false);
        final ToDoDatabase db = ToDoDatabase.getInstance(context);
         int uniqueId = intent.getIntExtra("unique", 0);
        //  AsyncTask.execute(() -> db.toDoDao().updateById(Integer.parseInt(getId), true));
         NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(message)
                .setContentText(String.valueOf(x))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainScreen.class), PendingIntent.FLAG_ONE_SHOT));
        manager.notify(uniqueId, notification.build());

        if (paused) {
            manager.cancel(uniqueId);
        } else {
            notification.setContentText(String.valueOf(System.currentTimeMillis()));
            manager.notify(uniqueId, notification.build());
        }
    }

}
