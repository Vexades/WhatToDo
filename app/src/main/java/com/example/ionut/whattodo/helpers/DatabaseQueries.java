package com.example.ionut.whattodo.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.ionut.whattodo.MainScreen;
import com.example.ionut.whattodo.Notifications;
import com.example.ionut.whattodo.database.ToDoDatabase;
import com.example.ionut.whattodo.database.ToDoModel;
import com.example.ionut.whattodo.widgets.SelectedDateNotifications;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class DatabaseQueries {
    private  Context context;
     private final Notifications notifications;
    private final SelectedDateNotifications selectedDateNotifications;

    public DatabaseQueries(Context context, SelectedDateNotifications selectedDateNotifications, Notifications notifications){
        this.context = context;
        this.notifications = notifications;
        this.selectedDateNotifications = selectedDateNotifications;
    }

    private final ToDoDatabase db = ToDoDatabase.getInstance(context);
    public void insertNewModel(ToDoModel toDoModel){
        AsyncTask.execute(() -> {
            db.toDoDao().insert(toDoModel);
            Intent i = new Intent(context, MainScreen.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Objects.requireNonNull(context).startActivity(i);
        });
    }

  /*  public void sendExpireNotification(){
        db.toDoDao().getAllModels().map(a -> a.get(a.size() - 1))
                .take(1)
                .subscribe(b -> {
                    int uniqueInt = ThreadLocalRandom.current().nextInt(1, 10000000 + 1);
                    Notifications notifications = new Notifications(context);
                    notifications.sendNotDoneNotification(b.getmName(),String.valueOf(b.getmId()),context,b.getmDate(),uniqueInt,1);
                  //  notifications.sendPeriodicNotificationMinutes(1,b.getmName(),String.valueOf(b.getmId()),context,b.getmDate());
                },throwable -> throwable.getLocalizedMessage());
    }*/
    public long aLong(){
       return selectedDateNotifications.finalTimeInMilli();
    }
    public void periodicNotif(){
        AsyncTask.execute(() -> {
            List<ToDoModel> modelList = db.toDoDao().getAllModelsNormal();
            ToDoModel b = modelList.get( modelList.size() - 1 );
            notifications.sendPeriodicNotification(b.getmName(),b.getmId(), context
                    ,selectedDateNotifications.finalTimeInMilli(),b.ismPause());
        });
        }
    public void sendExpireNotification(){
        AsyncTask.execute(() -> {
            int uniqueInt = ThreadLocalRandom.current().nextInt(1, 10000000 + 1);
            List<ToDoModel> modelList = db.toDoDao().getAllModelsNormal();
            ToDoModel b = modelList.get( modelList.size() - 1 );
            notifications.sendNotDoneNotification(b.getmName(),b.getmId(),context,b.getmDate(),uniqueInt,1);
        });
    }


    }





