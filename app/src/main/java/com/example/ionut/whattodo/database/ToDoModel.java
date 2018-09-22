package com.example.ionut.whattodo.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "todo_table")
public class ToDoModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NonNull
    public int mId;

    @ColumnInfo(name = "name")
    public String mName;

    @ColumnInfo(name = "paused")
    public boolean mPause;


    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "date")
    public Date mDate;

    @NonNull
    @ColumnInfo(name = "done")
    public boolean mDone;

    @ColumnInfo(name = "path")
    public String mPath;

    public ToDoModel(){}


    public ToDoModel(String mName, Date mDate, boolean mDone, String mPath, boolean mPause) {
        this.mName = mName;
        this.mDate = mDate;
        this.mDone = mDone;
        this.mPath = mPath;
        this.mPause = mPause;
    }

    public String getmName() {
        return mName;
    }

    public Date getmDate() {
        return mDate;
    }

    public int getmId() {
        return mId;
    }

    public boolean ismDone() {
        return mDone;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public void setmDone(boolean mDone) {
        this.mDone = mDone;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public boolean ismPause() {
        return mPause;
    }

    public void setmPause(boolean mPause) {
        this.mPause = mPause;
    }
}
