package com.example.ionut.whattodo.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.example.ionut.whattodo.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SelectedDateNotifications implements TextWatcher {
    private long date;
    private TextInputEditText hoursInput;
    private TextInputEditText minutesInput;
    private TextInputEditText daysInput;
    private Context context;
    private long convDays;
    private long convHours;
    private long convMinutes;
    private int justToKeepReffrence = 1;

    public SelectedDateNotifications(final Context context, final TextInputEditText daysInput, final TextInputEditText hoursInput, final TextInputEditText minutesInput){
        this.hoursInput = hoursInput;
        this.minutesInput = minutesInput;
        this.daysInput = daysInput;
        this.context = context;
        this.daysInput.addTextChangedListener(this);
        this.minutesInput.addTextChangedListener(this);
        this.hoursInput.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            if (!daysInput.getText().toString().equals("") && convertToDays() > 0) {
                if (Integer.valueOf(daysInput.getText().toString().trim()) > convertToDays()) {
                   justToKeepReffrence = 0;
                   tooLargeNumber(convertToDays(), Integer.parseInt(daysInput.getText().toString().trim()), "days");
                } else {
                    convDays = Integer.valueOf(daysInput.getText().toString().trim()) * 86400000;
                }
            }

            if (!hoursInput.getText().toString().equals("") && convertToHours() > 0) {
                if (Integer.valueOf(hoursInput.getText().toString().trim()) > convertToHours()) {
                   justToKeepReffrence = 0;
                    tooLargeNumber(convertToHours(), Integer.parseInt(hoursInput.getText().toString().trim()), "hours");
                } else {
                    convHours = Integer.valueOf(hoursInput.getText().toString().trim()) * 3600000;
                }
            }

            if (!minutesInput.getText().toString().equals("") && convertToMinutes() > 0) {
                if (Integer.valueOf(minutesInput.getText().toString().trim()) > convertToMinutes()) {
                   justToKeepReffrence = 0;
                    tooLargeNumber(convertToMinutes(), Integer.parseInt(minutesInput.getText().toString().trim()), "minutes");
                } else {
                    convMinutes = Integer.valueOf(minutesInput.getText().toString().trim()) * 60000;
                }
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        finalTimeInMilli();

    }

    public long finalTimeInMilli(){
        try {
            long totalDays;
            long totalHours;
            long totalMinutes;

            if(daysInput.getText().toString().trim().isEmpty()){
                totalDays = 0;
            }else {
                totalDays =  Integer.valueOf(daysInput.getText().toString().trim()) * 86400000;
            }

            if(hoursInput.getText().toString().trim().isEmpty()){
                totalHours = 0;
            }else {
                totalHours = Integer.valueOf(hoursInput.getText().toString().trim()) * 3600000;
            }

            if(minutesInput.getText().toString().isEmpty()){
                totalMinutes = 0;
            }else {
                totalMinutes = Integer.valueOf(minutesInput.getText().toString().trim()) * 60000;
            }

            long totalInput = totalDays + totalHours + totalMinutes;
            Date currentDate = new Date();
            Date finishedDate = new Date(date);
            float differenceInMilli = finishedDate.getTime() - currentDate.getTime();
            if (differenceInMilli >= totalInput ) {
                return totalInput;
            } else {
                if(justToKeepReffrence == 1) {
                    new AlertDialog.Builder(context)
                            .setTitle("Number is too large")
                            .setMessage("The date time for notifications period is too large!")
                            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
                justToKeepReffrence = 1;
                return -1;
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return -1;
    }




    public void setDate(long date) {
        this.date = date;

    }



    private void tooLargeNumber(int howManyTillFinish ,int userEntered, String typeOfDate){
        new AlertDialog.Builder(context)
                .setTitle("Number is too large")
                .setMessage( howManyTillFinish + " "+ typeOfDate + " till finish. You entered: "+userEntered+". Please enter a smaller value")
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    private int convertToMinutes() {
        if(date > 0) {
            Date currentDate = new Date();
            Date finishedDate = new Date(date);
            float differenceInMilli = finishedDate.getTime() - currentDate.getTime();
            int convertedMinutes = (int) (differenceInMilli / 60000);
            return convertedMinutes;
        }return 0;
    }

    private int convertToHours(){
        if(date > 0){
            Date currentDate = new Date();
            Date finishedDate = new Date(date);
            float differenceInMilli = finishedDate.getTime() - currentDate.getTime();
            long convertedHours = (long) (differenceInMilli / 3600000);
            return (int) convertedHours;
        }
        return 0;
    }


    private int convertToDays(){
        if(date > 0){
            Date currentDate = new Date();
            Date finishedDate = new Date(date);
            float differenceInMilli = finishedDate.getTime() - currentDate.getTime();
            long convertedHours = (long) (differenceInMilli / 86400000);
            return (int) convertedHours;
        }
        return 0;
    }


}
