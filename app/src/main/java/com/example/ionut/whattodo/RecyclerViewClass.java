package com.example.ionut.whattodo;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ionut.whattodo.database.ToDoDatabase;
import com.example.ionut.whattodo.database.ToDoModel;
import com.example.ionut.whattodo.fragments.fragmentePrelucrare.DetailFragment;
import com.example.ionut.whattodo.managers.BroadCastManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RecyclerViewClass extends RecyclerView.Adapter<RecyclerViewClass.MyHolder> {

    private final List<ToDoModel> toDoModels;
    private static final String DETAIL_FRAG_TAG = "detailFragment";


    private final Context context;
    public ImageView photo;
    long finalDateLong;


    public RecyclerViewClass(List<ToDoModel> toDoModels, Context context) {
        this.toDoModels = toDoModels;
        this.context = context;


    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main_fragment, viewGroup, false);
        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.bindWitouhtImage(toDoModels.get(i));


    }

    @Override
    public int getItemCount() {
        return toDoModels.size();
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private final TextView calendar_text;
        private final TextView time_text;
        final ImageView notification_off;
        final ImageView notification_on;
        private final TextView time_left;
        final TextView mTodo;
        private View colorIndicator;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            mTodo = itemView.findViewById(R.id.todoInfo);
            notification_off = itemView.findViewById(R.id.notifications_off);
            notification_on = itemView.findViewById(R.id.notifications_on);
            calendar_text = itemView.findViewById(R.id.calendar_text);
            time_text = itemView.findViewById(R.id.time_text);
            time_left = itemView.findViewById(R.id.time_left);
            colorIndicator = itemView.findViewById(R.id.verticalBar);
        }

        private void bindWitouhtImage(ToDoModel toDoModel) {
            itemView.setOnClickListener(v -> {
                MainScreen mainScreen = (MainScreen) context;
                Fragment fragment = DetailFragment.newInstance();
                mainScreen.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment, DETAIL_FRAG_TAG)
                        .addToBackStack(null).commit();
            });




            if (toDoModels.get(getAdapterPosition()).ismPause() && toDoModels.get(getAdapterPosition()).getmPeriodic() != 0) {
                notification_on.setVisibility(View.INVISIBLE);
                notification_off.setVisibility(View.VISIBLE);
                String ns = Context.NOTIFICATION_SERVICE;
                NotificationManager manager = (NotificationManager) context.getSystemService(ns);
                assert manager != null;
                manager.cancel(toDoModel.getmId());
                Intent intent = new Intent(context, BroadCastManager.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                        toDoModel.getmId(), intent, 0);
                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                assert am != null;
                am.cancel(pendingIntent);
                pendingIntent.cancel();
            } else if(toDoModels.get(getAdapterPosition()).getmPeriodic() == 0){
                notification_on.setVisibility(View.GONE);
                notification_off.setVisibility(View.GONE);
            }else {
                notification_on.setVisibility(View.VISIBLE);
                notification_off.setVisibility(View.INVISIBLE);
            }
            mTodo.setText(toDoModel.getmName());
            notification_on.setOnClickListener(v -> {
                ToDoDatabase db = ToDoDatabase.getInstance(context);
                AsyncTask.execute(() -> db.toDoDao().updateByIdPaused(toDoModels.get(getAdapterPosition()).getmId(), true));
                notification_on.setVisibility(View.GONE);
                notification_off.setVisibility(View.VISIBLE);
                String ns = Context.NOTIFICATION_SERVICE;
                NotificationManager manager = (NotificationManager) context.getSystemService(ns);
                assert manager != null;
                manager.cancel(toDoModel.getmId());
            });

            notification_off.setOnClickListener(v -> {
                ToDoDatabase db = ToDoDatabase.getInstance(context);
                AsyncTask.execute(() -> db.toDoDao().updateByIdPaused(toDoModels.get(getAdapterPosition()).getmId(), false));
                notification_off.setVisibility(View.GONE);
                notification_on.setVisibility(View.VISIBLE);
                sendPeriodicNotification(toDoModels.get(getAdapterPosition())
                .getmName(),toDoModels.get(getAdapterPosition()).getmId(),
                        context,toDoModels.get(getAdapterPosition()).getmPeriodic(),
                        false);
            });


            calendar_text.setText((formatOnlyDate(toDoModel.getmDate())));
            time_text.setText((formatOnlyTime(toDoModel.getmDate())));
             finalDateLong = (toDoModel.getmDate().getTime());
             time_left.setText(timeLeft(toDoModel.getmDate(),colorIndicator));

        }
    }

    private String formatOnlyDate(Date date) {
        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(date);
    }

    private String formatOnlyTime(Date date) {
        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(date);
    }

    @SuppressLint("CheckResult")
    private String timeLeft(Date date,View v) {
        long minute = 60000;
        long hour = 3600000;
        long day = 86400000;
        long converterd;




        long diff = date.getTime() - new Date().getTime();


        if (diff > minute && diff < hour) {
            converterd = diff / minute;
            if (converterd == 1) {
                io.reactivex.Observable.interval(1, TimeUnit.MINUTES)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(a -> notifyDataSetChanged());
                return String.valueOf(converterd) + " minute remaining";
            } else {
                io.reactivex.Observable.interval(1, TimeUnit.MINUTES)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(a -> {
                            long diffCurrent = finalDateLong - new Date().getTime();
                             if (diffCurrent < diff/2){
                                v.setBackgroundColor(Color.parseColor("#FF8E2020"));
                            }
                            notifyDataSetChanged();
                        });
                return String.valueOf(converterd) + " minutes remaining";
            }
        } else if (diff > hour && diff < day) {
            converterd = diff / hour;
            if (converterd == 1) {
                io.reactivex.Observable.interval(1, TimeUnit.MINUTES)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(a -> notifyDataSetChanged());
                return String.valueOf(converterd) + " hour remaining";
            } else {
                io.reactivex.Observable.interval(1, TimeUnit.HOURS)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(a -> notifyDataSetChanged());
                return String.valueOf(converterd) + " hours remaining";
            }
        } else if (diff > day) {
            converterd = diff / day;
            if (converterd == 1) {
                io.reactivex.Observable.interval(1, TimeUnit.HOURS)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(a -> notifyDataSetChanged());
                return String.valueOf(converterd) + " day remaining";
            } else {
                io.reactivex.Observable.interval(1, TimeUnit.DAYS)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(a -> notifyDataSetChanged());
                return String.valueOf(converterd) + " days remaining";
            }
        }
        return "Expired!";
    }

    public void sendPeriodicNotification(String name, int idOfModel, Context context,
                                         long timeOfPeriodicNotif, boolean paused){
        Intent intent = new Intent(context, BroadCastManager.class);
        intent.putExtra("toDoName", name);
        intent.putExtra("id", idOfModel);
        intent.putExtra("unique", idOfModel);
        intent.putExtra("paused",paused);
        intent.putExtra("test",System.currentTimeMillis());
        AlarmManager alarm = (AlarmManager) Objects.requireNonNull(context).getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,idOfModel,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        assert alarm != null;
        alarm.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis()+timeOfPeriodicNotif, timeOfPeriodicNotif, pendingIntent);
    }


}
