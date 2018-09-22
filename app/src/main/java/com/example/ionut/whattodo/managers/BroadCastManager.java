package com.example.ionut.whattodo.managers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.ionut.whattodo.MainScreen;
import com.example.ionut.whattodo.R;
import com.example.ionut.whattodo.database.ToDoDatabase;
import com.example.ionut.whattodo.database.ToDoModel;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.fuseable.ScalarCallable;
import io.reactivex.schedulers.Schedulers;


 public class BroadCastManager extends BroadcastReceiver {

    public static NotificationManager manager ;
    int uniqueId ;
    boolean paused;

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("toDoName");
        int getId = intent.getIntExtra("id", 0);
        long x = intent.getLongExtra("test", 0);
        paused = intent.getBooleanExtra("paused", false);
        final ToDoDatabase db = ToDoDatabase.getInstance(context);
        uniqueId = intent.getIntExtra("unique", 0);
        //  AsyncTask.execute(() -> db.toDoDao().updateById(Integer.parseInt(getId), true));
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
