package com.example.ionut.whattodo.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.ionut.whattodo.MainScreen;
import com.example.ionut.whattodo.Notifications;
import com.example.ionut.whattodo.database.ToDoDatabase;
import com.example.ionut.whattodo.database.ToDoModel;
import com.example.ionut.whattodo.widgets.SelectedDateNotifications;
import com.example.ionut.whattodo.widgets.TimeWrapper;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class RxQuery {
    Context context ;
     private Notifications notifications;
    private SelectedDateNotifications selectedDateNotifications;

    public RxQuery(Context context, SelectedDateNotifications selectedDateNotifications, Notifications notifications){
        this.context = context;
        this.notifications = notifications;
        this.selectedDateNotifications = selectedDateNotifications;
    }

    final ToDoDatabase db = ToDoDatabase.getInstance(context);
    public void insertNewModel(ToDoModel toDoModel){
        Flowable.just(db)
                .subscribeOn(Schedulers.io())
                .subscribe(item -> {
                    item.toDoDao().insert(toDoModel);
                    Intent i = new Intent(context, MainScreen.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                });
    }

    public void sendExpireNotification(){
        db.toDoDao().getAllModels().map(a -> a.get(a.size() - 1))
                .take(1)
                .subscribe(b -> {
                    int uniqueInt = ThreadLocalRandom.current().nextInt(1, 10000000 + 1);
                    Notifications notifications = new Notifications(context);
                    notifications.sendNotDoneNotification(b.getmName(),String.valueOf(b.getmId()),context,b.getmDate(),uniqueInt);
                  //  notifications.sendPeriodicNotificationMinutes(1,b.getmName(),String.valueOf(b.getmId()),context,b.getmDate());
                },throwable -> throwable.getLocalizedMessage());
    }


    public long aLong(){
       return selectedDateNotifications.finalTimeInMilli();
    }

    public void testPeriodicNotif(){

        AsyncTask.execute(() -> {
            List<ToDoModel> modelList = db.toDoDao().getAllModelsNormal();
            ToDoModel b = modelList.get( modelList.size() - 1 );
            notifications.sendPeriodicNotification(b.getmName(),b.getmId(), context
                    ,selectedDateNotifications.finalTimeInMilli(),b.ismPause());
        });
        }

    }





