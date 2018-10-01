package com.example.ionut.whattodo.widgets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ionut.whattodo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TimeWrapper implements   DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private final TextView dateTextView;
    private Date currentDate;
    private final Context context;
    private boolean isTimeChosen = false;
    private int firstTimeRun = 1;
    private boolean dateChosed = false;
    private final SelectedDateNotifications selectedDateNotifications;
    private String setTimeVariable;
    private LinearLayout linearLayout;

    public boolean isDateChosed() {
        return dateChosed;
    }

    private final TextView view;


    public TimeWrapper(TextView textView, TextView dateTextView, Context context, SelectedDateNotifications selectedDateNotifications, LinearLayout linearLayout) {
        this.context = context;
        this.dateTextView = dateTextView;
        this.view = textView;
        setDate();
        this.linearLayout = linearLayout;

        this.selectedDateNotifications = selectedDateNotifications;


    }


    private void setDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("cccc \ndd.MM.yyyy, H:mm", Locale.US);
        if (currentDate == null) {
            currentDate = new Date();
            view.setText(format.format(currentDate));
        } else {
            if(isTimeChosen) {
                view.setText(format.format(currentDate));
            }else {
                view.setText(format.format(new Date()));
                isTimeChosen = false;
            }
        }

    }

    public int isDateDifferenceNegative(){
        Date date = new Date();
        if(currentDate.getTime() < date.getTime()){
            return -100;
        }else {
            return 0;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        setTimePicker();
        if(firstTimeRun == 1){
            currentDate = calendar.getTime();
            firstTimeRun = 0;
        }

        if(isTimeChosen){
            currentDate = calendar.getTime();
        }else if(firstTimeRun == 0){
            firstTimeRun = -1;
            setDate();
        }else {
            setDate();
        }




    }

    public void initialize(Activity activity){
        @SuppressLint("InflateParams") View v = activity.getLayoutInflater().inflate(R.layout.single_item_view,null);
        LinearLayout linearLayout = v.findViewById(R.id.notif_layout);
        if(currentDate.getTime() != 0){
            linearLayout.setVisibility(VISIBLE);
        }else {
            linearLayout.setVisibility(GONE);
        }
    }

    public void openDatePicker() {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);
        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void setTimePicker(){
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);
        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        currentDate = calendar.getTime();
        dateTextView.setVisibility(VISIBLE);
        dateTextView.setText(getTimeTillFinish(currentDate));
        timeSetVariable(getTimeTillFinish(currentDate));
        isTimeChosen = true;
        setDate();
        selectedDateNotifications.setDate(currentDate.getTime());
        linearLayout.setVisibility(VISIBLE);

        //Arata editurile tastaturii
        isTimeChosen = true;
    }

    public void timeSetVariable(String setTimeVariable){
        this.setTimeVariable = setTimeVariable;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    private String getTimeTillFinish(Date date) {
        final int oneHour = 3600000;
        final int oneDay = 86400000;
        int reminderMinutes;
        int reminderHours;
        Date currentDate = new Date();
        Date finishedDate = new Date(date.getTime());
        float differenceInMilli = finishedDate.getTime() - currentDate.getTime();
        if (differenceInMilli <= 0) {
            Toast.makeText(context, "Date chose in in the past", Toast.LENGTH_SHORT).show();
        } else {
            if (differenceInMilli < oneHour) {
                int convertedMinutes = (int) (differenceInMilli / 60000);
                if (convertedMinutes < 10) {
                    return String.valueOf("Due time in 0" + convertedMinutes + ":00 Minutes");
                } else {
                    return String.valueOf("Due in " + convertedMinutes + ":00 Minutes");
                }
            } else if (differenceInMilli >= oneHour && differenceInMilli <= oneDay) {
                long convertedHours = (long) (differenceInMilli / 3600000);
                long convertedReminder = (long) (differenceInMilli % 3600000);
                reminderMinutes = (int) (convertedReminder / 60000);
                return String.valueOf("Due in " + convertedHours + " Hours and " + reminderMinutes + " minutes");
            } else if (differenceInMilli >= oneDay) {
                long convertedDays = (long) (differenceInMilli / 86400000);
                long convertedReminder = (long) (differenceInMilli % 86400000);
                reminderHours = (int) (convertedReminder / 3600000);
                long convertedMinutes = convertedReminder % 3600000;
                int minutes = (int) (convertedMinutes / 60000);
                return String.valueOf("Due in " + convertedDays + " days " + reminderHours + " hours and " + minutes + " minutes");
            }
        }
        return null;
    }


}