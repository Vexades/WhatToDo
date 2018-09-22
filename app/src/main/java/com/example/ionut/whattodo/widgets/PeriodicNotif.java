package com.example.ionut.whattodo.widgets;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.ionut.whattodo.managers.BroadCastManager;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class PeriodicNotif {
    public static final int ISVALID = 0;
    public static final int ISNOTVALID = 1;



    public PeriodicNotif(){

    }

    public void sendPeriodicNotificationMinutes(int notificationID,
                                                String name,
                                                String idOfModel,
                                                Context context,
                                                Date finalDate) {
        int uniqueInt = ThreadLocalRandom.current().nextInt(1, 10000000 + 1);
        Intent intent = new Intent(context, BroadCastManager.class);
        AlarmManager alarm = (AlarmManager) Objects.requireNonNull(context).getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getService(context, uniqueInt, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        intent.putExtra("notificationId", notificationID);
        intent.putExtra("toDoName", name);
        intent.putExtra("idd", idOfModel);
        intent.putExtra("unique", uniqueInt);
        intent.putExtra("date", finalDate.getTime());
        assert alarm != null;
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, alarmIntent);
    }

}
