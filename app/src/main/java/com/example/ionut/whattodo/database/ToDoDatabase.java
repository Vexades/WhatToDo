package com.example.ionut.whattodo.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {ToDoModel.class},version = 14,exportSchema = false)
public abstract class ToDoDatabase extends RoomDatabase{
    public abstract ToDoDao toDoDao();


    private static ToDoDatabase INSTANCE;


    public static ToDoDatabase getInstance(final Context context){
        if(INSTANCE == null){
            synchronized (ToDoDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),ToDoDatabase.class,"todo_database").build();
                }
            }
        }
        return INSTANCE;
    }



}
