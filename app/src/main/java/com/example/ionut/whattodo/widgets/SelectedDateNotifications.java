package com.example.ionut.whattodo.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;

import java.util.Date;
import java.util.Objects;

public class SelectedDateNotifications implements TextWatcher {
    private long date;
    private final TextInputEditText hoursInput;
    private final TextInputEditText minutesInput;
    private final TextInputEditText daysInput;
    private final Context context;
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
            if (!Objects.requireNonNull(daysInput.getText()).toString().equals("") && convertToDays() > 0) {
                if (Integer.valueOf(daysInput.getText().toString().trim()) > convertToDays()) {
                   justToKeepReffrence = 0;
                   tooLargeNumber(convertToDays(), Integer.parseInt(daysInput.getText().toString().trim()), "days");
                }
            }

            if (!Objects.requireNonNull(hoursInput.getText()).toString().equals("") && convertToHours() > 0) {
                if (Integer.valueOf(hoursInput.getText().toString().trim()) > convertToHours()) {
                   justToKeepReffrence = 0;
                    tooLargeNumber(convertToHours(), Integer.parseInt(hoursInput.getText().toString().trim()), "hours");
                }
            }

            if (!Objects.requireNonNull(minutesInput.getText()).toString().equals("") && convertToMinutes() > 0) {
                if (Integer.valueOf(minutesInput.getText().toString().trim()) > convertToMinutes()) {
                   justToKeepReffrence = 0;
                    tooLargeNumber(convertToMinutes(), Integer.parseInt(minutesInput.getText().toString().trim()), "minutes");
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

            if(Objects.requireNonNull(daysInput.getText()).toString().trim().isEmpty()){
                totalDays = 0;
            }else {
                totalDays =  Integer.valueOf(daysInput.getText().toString().trim()) * 86400000;
            }

            if(Objects.requireNonNull(hoursInput.getText()).toString().trim().isEmpty()){
                totalHours = 0;
            }else {
                totalHours = Integer.valueOf(hoursInput.getText().toString().trim()) * 3600000;
            }

            if(Objects.requireNonNull(minutesInput.getText()).toString().isEmpty()){
                totalMinutes = 0;
            }else {
                totalMinutes = Integer.valueOf(minutesInput.getText().toString().trim()) * 60000;
            }

            long totalInput = totalDays + totalHours + totalMinutes;
            Date currentDate = new Date();
            Date finishedDate = new Date(date);
            float differenceInMilli = finishedDate.getTime() - currentDate.getTime();
            if (Objects.requireNonNull(daysInput.getText()).toString().trim().isEmpty() &&
                    Objects.requireNonNull(minutesInput.getText()).toString().trim().isEmpty()
                    && Objects.requireNonNull(hoursInput.getText()).toString().trim().isEmpty()) {
                return 0;
            } else if(differenceInMilli >= totalInput){
                return totalInput;
            }else {
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
                .setNegativeButton("Dismiss", (dialog, which) -> dialog.dismiss()).show();
    }


    private int convertToMinutes() {
        if(date > 0) {
            Date currentDate = new Date();
            Date finishedDate = new Date(date);
            float differenceInMilli = finishedDate.getTime() - currentDate.getTime();
            return (int) (differenceInMilli / 60000);
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
