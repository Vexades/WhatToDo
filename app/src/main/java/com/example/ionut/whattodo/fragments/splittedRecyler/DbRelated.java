package com.example.ionut.whattodo.fragments.splittedRecyler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.ionut.whattodo.MainScreen;
import com.example.ionut.whattodo.RecyclerViewClass;
import com.example.ionut.whattodo.database.ToDoDatabase;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DbRelated {

    private final Context context;
    private final RecyclerView recyclerView;


    public DbRelated(Context context, RecyclerView recyclerView){
        this.context = context;
        this.recyclerView = recyclerView;
    }
    @SuppressLint("CheckResult")
    public void updateUINotDoneItems() {
        if(context instanceof MainScreen){
            MainScreen mainScreen = (MainScreen)context;
            Objects.requireNonNull(mainScreen.getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        }
        final ToDoDatabase db = ToDoDatabase.getInstance(context);
        db.toDoDao().getAllByDone(false)
                .subscribeOn(Schedulers.io())
                 // .doOnEach(listaa -> db.toDoDao().deleteAllModel())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list ->{
                    RecyclerViewClass adapter = new RecyclerViewClass(list, context);
                    recyclerView.setAdapter(adapter);

                });
    }
    @SuppressLint("CheckResult")
    public void updateDoneItems(){
        if(context instanceof MainScreen){
            MainScreen mainScreen = (MainScreen)context;
            Objects.requireNonNull(mainScreen.getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        }
        final ToDoDatabase db = ToDoDatabase.getInstance(context);
        db.toDoDao().getAllByDone(true)
                .subscribeOn(Schedulers.computation())
                 // .doOnEach(listaa -> db.toDoDao().deleteAllModel())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list ->{
                    RecyclerViewClass adapter = new RecyclerViewClass(list, context);
                    recyclerView.setAdapter(adapter);
                });

    }
}
