package com.example.ionut.whattodo.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;

public class TextWatcherWrapper implements TextWatcher, AdapterView.OnItemSelectedListener {
    private  int workOrNot ;

    private Context context;
    private int numberOfNot;
    private PeriodicNotif periodicNotif;
    private TextInputEditText textInputEditText;
    private Date  date;
    private Spinner spinner;
    private int getPosition;
    private int oneNotification = 0;

    public TextWatcherWrapper(Context context, TextInputEditText textInputEditText,Spinner spinner){
        this.textInputEditText = textInputEditText;
        this.context = context;
        this.spinner = spinner;
        this.spinner.setOnItemSelectedListener(this);
        this.date = new Date();

    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    public int getGetPosition() {
        return getPosition;
    }

    public void setGetPosition(int getPosition) {
        this.getPosition = getPosition;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate(){
        return this.date;
    }
    @Override
    public void afterTextChanged(Editable s) {
        setPositionTime(getGetPosition());
    }

    public int getNumberOfNot(){
        return this.numberOfNot;
    }

    public void tooLargeNumber(int userEntered, String typeOfDate){
        new AlertDialog.Builder(context)
                .setTitle("Number is too large")
                .setMessage( typeOfDate+ " till finish. You entered: "+userEntered+". Please enter a smaller value")
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    private void setPositionTime(int positionTime){
        switch (positionTime){
            case 0:
                if(textInputEditText.getText().toString() != null && !textInputEditText.getText().toString().equals("")) {
                    if (Integer.valueOf(textInputEditText.getText().toString().trim()) > convertToMinutes()) {
                        if(oneNotification == 0) {
                            tooLargeNumber(Integer.valueOf(textInputEditText.getText().toString().trim()), "Minutes");
                            oneNotification ++;
                            workOrNot = 0;
                        }
                        break;
                    } else {
                        workOrNot = 1;
                        oneNotification = 0;
                       break;
                    }
                }
            case 1:
                if(textInputEditText.getText().toString() != null && !textInputEditText.getText().toString().equals("")) {
                    if (Integer.valueOf(textInputEditText.getText().toString().trim()) > convertToHours()) {
                        if(oneNotification == 0) {
                            tooLargeNumber(Integer.valueOf(textInputEditText.getText().toString().trim()), "Hours");
                            oneNotification++;
                            workOrNot = 0;
                        }
                        oneNotification ++;
                        break;
                    } else {
                         workOrNot = 1;
                        oneNotification = 0;
                        break;
                    }
                }
            case 2:
                if(textInputEditText.getText().toString() != null && !textInputEditText.getText().toString().equals("")) {
                    if (Integer.valueOf(textInputEditText.getText().toString().trim()) > convertToDays()) {
                        if(oneNotification == 0) {
                            tooLargeNumber(Integer.valueOf(textInputEditText.getText().toString().trim()), "Days");
                            oneNotification ++;
                            workOrNot = 0;
                        }
                        break;
                    } else {
                        workOrNot = 1;
                        oneNotification = 0;
                       break;

                    }
                }
        }
        }

    public int getWorkOrNot() {
        Toast.makeText(context, String.valueOf(workOrNot), Toast.LENGTH_SHORT).show();
        return workOrNot;
    }



    private int convertToMinutes() {
        Date currentDate = new Date();
        Date finishedDate = new Date(date.getTime());
        float differenceInMilli = finishedDate.getTime() - currentDate.getTime();
        int convertedMinutes = (int) (differenceInMilli / 60000);
        return convertedMinutes;
    }

    private int convertToHours(){
        Date currentDate = new Date();
        Date finishedDate = new Date(date.getTime());
        float differenceInMilli = finishedDate.getTime() - currentDate.getTime();
        long convertedHours = (long) (differenceInMilli / 3600000);
        return (int) convertedHours;
    }

    private int convertToDays(){
        Date currentDate = new Date();
        Date finishedDate = new Date(date.getTime());
        float differenceInMilli = finishedDate.getTime() - currentDate.getTime();
        long convertedHours = (long) (differenceInMilli / 86400000);
        return (int) convertedHours;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setGetPosition(position);
      //  setPositionTime(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


