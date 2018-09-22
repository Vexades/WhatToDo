package com.example.ionut.whattodo.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ionut.whattodo.Notifications;

import java.util.Date;
import java.util.List;


public class SpinnerWrapper {

    private String[] onlyMinutes = {"Minutes"};
    private String[] minutesAndHours = {"Minutes","Hours"};
    private String[] full = {"Minutes","Hours", "Days"};
    private Context context;
    private Spinner spinner;
    private int position;
    private Date date;
    private TextInputEditText textInputEditText;
    private Notifications notifications;


    public SpinnerWrapper(Context context, Spinner spinner){
        this.context = context;
        this.spinner = spinner;
    }

    public void initialize(int type){
        if(type == 0) {
          ArrayAdapter  adapterView = new ArrayAdapter(context, android.R.layout.simple_spinner_item, onlyMinutes);
            spinner.setAdapter(adapterView);
        }else if(type == 1){
            ArrayAdapter  adapterView = new ArrayAdapter(context, android.R.layout.simple_spinner_item, minutesAndHours);
            spinner.setAdapter(adapterView);
        }else if(type == 2){
            ArrayAdapter  adapterView = new ArrayAdapter(context, android.R.layout.simple_spinner_item, full);
            spinner.setAdapter(adapterView);
        }
    }

    public void setTextInputEditText(TextInputEditText textInputEditText) {
        this.textInputEditText = textInputEditText;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
