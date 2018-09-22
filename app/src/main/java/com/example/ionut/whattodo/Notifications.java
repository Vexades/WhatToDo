package com.example.ionut.whattodo;

import android.accessibilityservice.GestureDescription;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ionut.whattodo.managers.BroadCastManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class Notifications {
    private Context context;
    public static final int MINUTES = 0;
    public static final int HOURS = 1;
    public static final int DAYS = 2;



    public Notifications(Context context) {
        this.context = context;

    }


    public void sendNotDoneNotification( String name, String idOfModel, Context context, Date finalDate, int uniqueInt) {
        int uniqueId ;
        if(uniqueInt == 0){
            uniqueId = 0 + 100000;
        }else {
            uniqueId = uniqueInt * 100000;
        }
        Intent intent = new Intent(context, BroadCastManager.class);
        intent.putExtra("toDoName", name);
        intent.putExtra("id", idOfModel);
        intent.putExtra("unique", uniqueId);
        intent.putExtra("date", finalDate.getTime());
        AlarmManager alarm = (AlarmManager) Objects.requireNonNull(context).getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, uniqueInt, intent, PendingIntent.FLAG_ONE_SHOT);
        Objects.requireNonNull(alarm).set(AlarmManager.RTC_WAKEUP, finalDate.getTime(), alarmIntent);
    }

    public void sendPeriodicNotification(String name, int idOfModel, Context context,
                                         long timeOfPeriodicNotif, boolean paused){
        Intent intent = new Intent(context, BroadCastManager.class);
        intent.putExtra("toDoName", name);
        intent.putExtra("id", idOfModel);
        intent.putExtra("unique", idOfModel);
        intent.putExtra("paused",paused);
        intent.putExtra("test",System.currentTimeMillis());
        AlarmManager alarm = (AlarmManager)Objects.requireNonNull(context).getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,idOfModel,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis()+timeOfPeriodicNotif, timeOfPeriodicNotif, pendingIntent);
    }



    public String getTimeTillFinish(Date date) {
        final int oneHour = 3600000;
        final int oneDay = 86400000;
        int reminderMinutes;
        int reminderHours;
        Date currentDate = new Date();
        Date finishedDate = new Date(date.getTime());

        float differenceInMilli = finishedDate.getTime() - currentDate.getTime();
        if (differenceInMilli <= 0) {
            Toast.makeText(context, "Date chose in in the past", Toast.LENGTH_SHORT).show();
        } else {
            if (differenceInMilli < oneHour) {
                int convertedMinutes = (int) (differenceInMilli / 60000);
                if (convertedMinutes < 10) {
                    return String.valueOf("Due time in 0" + convertedMinutes + ":00 Minutes");
                } else {
                    return String.valueOf("Due in " + convertedMinutes + ":00 Minutes");
                }
            } else if (differenceInMilli >= oneHour && differenceInMilli <= oneDay) {
                long convertedHours = (long) (differenceInMilli / 3600000);
                long convertedReminder = (long) (differenceInMilli % 3600000);
                reminderMinutes = (int) (convertedReminder / 60000);
                return String.valueOf("Due in " + convertedHours + " Hours and " + reminderMinutes + " minutes");
            } else if (differenceInMilli >= oneDay) {
                long convertedDays = (long) (differenceInMilli / 86400000);
                long convertedReminder = (long) (differenceInMilli % 86400000);
                reminderHours = (int) (convertedReminder / 3600000);
                long convertedMinutes = convertedReminder % 3600000;
                int minutes = (int) (convertedMinutes / 60000);
                return String.valueOf("Due in " + convertedDays + " days " + reminderHours + " hours and " + minutes + " minutes");
            }
        }
        return null;
    }

    public int returnMaxTime(Date date) {
        final int oneHour = 3600000;
        final int oneDay = 86400000;
        Date currentDate = new Date();
        Date finishedDate = new Date();
        finishedDate.setTime(date.getTime());

        float differenceInMilli = finishedDate.getTime() - currentDate.getTime();
        if (differenceInMilli <= 0) {
            Toast.makeText(context, "Date chose in in the past", Toast.LENGTH_SHORT).show();
        } else {
            if (differenceInMilli < oneHour) {
                return MINUTES;
            } else if ( differenceInMilli >= oneHour && differenceInMilli <= oneDay) {
                return HOURS;
            } else if (differenceInMilli >= oneDay) {
                return DAYS;
            }
        }
        return 0;
    }

    public int convertToMinutes(Date date) {
        Date currentDate = new Date();
        Date finishedDate = new Date(date.getTime());
        float differenceInMilli = finishedDate.getTime() - currentDate.getTime();
            int convertedMinutes = (int) (differenceInMilli / 60000);
            return convertedMinutes;
    }

    public int convertToHours(Date date){
        Date currentDate = new Date();
        Date finishedDate = new Date(date.getTime());
        float differenceInMilli = finishedDate.getTime() - currentDate.getTime();
            long convertedHours = (long) (differenceInMilli / 3600000);
            return (int) convertedHours;
}

   public int convertToDays(Date date){
       Date currentDate = new Date();
       Date finishedDate = new Date(date.getTime());
       float differenceInMilli = finishedDate.getTime() - currentDate.getTime();
       long convertedHours = (long) (differenceInMilli / 86400000);
       return (int) convertedHours;
   }

}
