package com.example.ionut.whattodo.widgets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ionut.whattodo.Notifications;
import com.example.ionut.whattodo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TimeWrapper implements   DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private ImageView imageView;
    private TextView dateTextView;
    private Date currentDate;
    private Context context;
    private boolean isTimeChosen = false;
    private int firstTimeRun = 1;
    private AdapterView adapterView;
    public int returnMaxType;
    private  SelectedDateNotifications selectedDateNotifications;




    private TextView view;


    public TimeWrapper(final ImageView imageView, TextView textView, TextView dateTextView, Context context, SelectedDateNotifications selectedDateNotifications) {
        this.imageView = imageView;
        this.context = context;
        this.dateTextView = dateTextView;
        this.view = textView;
        setDate(getCurrentDate());
        returnMaxType = 0;
        this.selectedDateNotifications = selectedDateNotifications;

    }

    public TimeWrapper(){

    }

    private void setDate(Date date) {
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

    public String getDateFormatted(Date currentDate) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("cccc \ndd.MM.yyyy, H:mm", Locale.US);
        return format.format(currentDate);
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
            setDate(currentDate);
        }else {
            setDate(new Date());
        }




    }

    public void initialize(Activity activity){
        View v = activity.getLayoutInflater().inflate(R.layout.single_item_view,null);
        LinearLayout linearLayout = v.findViewById(R.id.linearSelections);
        if(currentDate.getTime() != 0){
            linearLayout.setVisibility(VISIBLE);
        }else {
            linearLayout.setVisibility(GONE);
        }
    }

    public int returnMaxTime(){
        Notifications notifications = new Notifications(context);
        if(currentDate == null){
            return notifications.returnMaxTime(new Date());
        }else {
            return notifications.returnMaxTime(currentDate);
        }
    }

    public void openDatePicker() {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);
        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));;
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
        Notifications notifications = new Notifications(context);
        dateTextView.setVisibility(VISIBLE);
        dateTextView.setText(notifications.getTimeTillFinish(currentDate));
        isTimeChosen = true;
        setDate(currentDate);
        selectedDateNotifications.setDate(currentDate.getTime());
        returnMaxType = returnMaxTime();
    }




    public Date getCurrentDate() {
        return currentDate;
    }


}