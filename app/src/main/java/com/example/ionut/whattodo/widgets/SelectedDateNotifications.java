package com.example.ionut.whattodo.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.ionut.whattodo.helpers.SelectedDateNotificationEnum;

import java.util.Date;
import java.util.Objects;

public class SelectedDateNotifications implements TextWatcher{
    private long date;
    private TextInputEditText hoursInput;
    private  TextInputEditText minutesInput;
    private  TextInputEditText daysInput;
    private final Context context;

    public SelectedDateNotifications( Context context){
        this.context = context;
    }


    public TextInputEditText getHoursInput() {
        return hoursInput;
    }

    public TextInputEditText getMinutesInput() {
        return minutesInput;
    }

    public TextInputEditText getDaysInput() {
        return daysInput;
    }

    public void setHoursInput(TextInputEditText textInputEditText){
        this.hoursInput = textInputEditText;
        this.hoursInput.addTextChangedListener(this);
    }

    public void setMinutesInput(TextInputEditText minutesInput) {
        this.minutesInput = minutesInput;
        this.minutesInput.addTextChangedListener(this);
    }

    public void setDaysInput(TextInputEditText daysInput) {
        this.daysInput = daysInput;
        this.daysInput.addTextChangedListener(this);
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
            if (!Objects.requireNonNull(getDaysInput().getText()).toString().equals("")  && getDate() > 0) {
                if (Integer.valueOf(getDaysInput().getText().toString().trim()) > convertToDays()) {

                   tooLargeNumber(convertToDays(), Integer.parseInt(getDaysInput().getText().toString().trim()), "days");
                }
            }

            if (!Objects.requireNonNull(getHoursInput().getText()).toString().equals("")  && getDate() > 0) {
                if (Integer.valueOf(getHoursInput().getText().toString().trim()) > convertToHours()) {

                    tooLargeNumber(convertToHours(), Integer.parseInt(getHoursInput().getText().toString().trim()), "hours");
                }
            }

            if (!Objects.requireNonNull(minutesInput.getText()).toString().equals("") && getDate() > 0) {
                if (Integer.valueOf(minutesInput.getText().toString().trim()) > convertToMinutes()) {

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

            if(Objects.requireNonNull(getDaysInput().getText()).toString().trim().isEmpty()){
                totalDays = 0;
            }else {
                totalDays =  Integer.valueOf(getDaysInput().getText().toString().trim()) * 86400000;
            }

            if(Objects.requireNonNull(getHoursInput().getText()).toString().trim().isEmpty()){
                totalHours = 0;
            }else {
                totalHours = Integer.valueOf(getHoursInput().getText().toString().trim()) * 3600000;
            }

            if(Objects.requireNonNull(getMinutesInput().getText()).toString().isEmpty()){
                totalMinutes = 0;
            }else {
                totalMinutes = Integer.valueOf(getMinutesInput().getText().toString().trim()) * 60000;
            }

            long totalInput = totalDays + totalHours + totalMinutes;
            Date currentDate = new Date();
            Date finishedDate = new Date(date);
            float differenceInMilli = finishedDate.getTime() - currentDate.getTime();
            if (Objects.requireNonNull(getDaysInput().getText()).toString().trim().isEmpty() &&
                    Objects.requireNonNull(getMinutesInput().getText()).toString().trim().isEmpty()
                    && Objects.requireNonNull(getHoursInput().getText()).toString().trim().isEmpty()) {
                return 0;
            } else if( Integer.valueOf(getMinutesInput().getText().toString()) < 1 ){
                return 10;
            }else if(differenceInMilli >= totalInput ){
                SelectedDateNotificationEnum.INSTANCE.setDate(totalInput);
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

    public long getDate() {
        return date;
    }

    private void tooLargeNumber(int howManyTillFinish , int userEntered, String typeOfDate){
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
