package com.example.ionut.whattodo.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface  ToDoDao {
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)

    @Insert
    void insert(ToDoModel...toDoModels);


    @Query("delete  from todo_table")
    void deleteAllModel();


    @Query("select * from todo_table")
    Flowable<List<ToDoModel>> getAllModels();

    @Query("select * from todo_table")
    List<ToDoModel> getAllModelsNormal();

    @Query("select * from todo_table where id= :itemId")
    Flowable<ToDoModel> getItemById(int itemId);

    @Query("select * from todo_table where id= :itemId")
    ToDoModel getItemByIdSimple(int itemId);


    @Query("update todo_table set done= :done   where id= :id")
    void updateById(int id, boolean done);

    @Query("update todo_table set paused= :paused where id = :id")
    void updateByIdPaused(int id, boolean paused);


    @Query("update todo_table set name= :name, date= :date, path= :path  where id= :id")
    void updateAllById(int id, String name, long date , String path);

    @Query("delete from todo_table where id= :id")
    void deleteItemById(int id);

    @Query("select * from todo_table where done= :done")
    Flowable<List<ToDoModel>> getAllByDone(boolean done);


}
