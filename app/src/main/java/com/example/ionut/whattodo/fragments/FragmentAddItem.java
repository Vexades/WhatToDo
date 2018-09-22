package com.example.ionut.whattodo.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ionut.whattodo.MainScreen;
import com.example.ionut.whattodo.Notifications;
import com.example.ionut.whattodo.R;
import com.example.ionut.whattodo.database.ToDoDatabase;
import com.example.ionut.whattodo.database.ToDoModel;
import com.example.ionut.whattodo.helpers.RxQuery;
import com.example.ionut.whattodo.widgets.PeriodicNotif;
import com.example.ionut.whattodo.widgets.SelectedDateNotifications;
import com.example.ionut.whattodo.widgets.SpinnerWrapper;
import com.example.ionut.whattodo.widgets.TakePic;
import com.example.ionut.whattodo.widgets.TextWatcherWrapper;
import com.example.ionut.whattodo.widgets.TimeWrapper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.concurrent.ThreadLocalRandom;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

//Implementam interfata din care luam date de la alt fragment
public class FragmentAddItem extends Fragment  {

    public static final int PERMISSION_CAMERA = 1;


    private TextInputEditText description;
    private TextInputEditText select_notif;
    private TakePic takePic;
    private String mPhoto;
    private TimeWrapper timeWrapper;
    private ImageView photo;
    private ImageView calendarImageView;
    private TextInputEditText daysSelect;
    private TextInputEditText hoursSelect;
    private TextInputEditText minutesSelect;
    private TextView date;
    private boolean isInsertedModel = false;
    private TextView toDoInfo;



    @SuppressLint({"CheckResult", "CutPasteId"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.single_item_view, container, false);
        View recyclerView = inflater.inflate(R.layout.activity_main_fragment,null,false);
        Button mSave = v.findViewById(R.id.save);
        description = v.findViewById(R.id.description);
        daysSelect = v.findViewById(R.id.textDays);
        hoursSelect = v.findViewById(R.id.textHours);
        minutesSelect = v.findViewById(R.id.textMinutes);
        date = v.findViewById(R.id.date);


        SelectedDateNotifications selectedDateNotifications = new SelectedDateNotifications(getContext(),daysSelect,hoursSelect,minutesSelect);
        TextView dateTextView = v.findViewById(R.id.dateTextView);
        FloatingActionButton photo_button = v.findViewById(R.id.photo_button);
        photo = v.findViewById(R.id.photo);
        takePic = new TakePic(photo_button,this);
        TimeWrapper timeWrapper = new TimeWrapper(calendarImageView,date,dateTextView,getContext(),selectedDateNotifications);

        Notifications notifications = new Notifications(getContext());
        TextView date = v.findViewById(R.id.date);
        calendarImageView = v.findViewById(R.id.calendarImageView);
        calendarImageView.setOnClickListener(v1 -> {
            timeWrapper.openDatePicker();
            if(getActivity() instanceof MainScreen){
                MainScreen screen = (MainScreen)getActivity();
                timeWrapper.initialize(screen);
            }
        });


        //Daca fragmetnul apartine activitaii MainScreen, arata butonul
        if (getActivity() instanceof MainScreen) {
            MainScreen mainScreen = (MainScreen) getActivity();
            Objects.requireNonNull(mainScreen.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        ToDoDatabase db = ToDoDatabase.getInstance(getContext());

        //Introduce in baza de date
        mSave.setOnClickListener(view -> {
            ToDoModel newModel = new ToDoModel(Objects.requireNonNull(description.getText()).toString().trim(), timeWrapper.getCurrentDate(), false, mPhoto,false);
            String aaa = description.getText().toString().trim();
            if (description.getText().toString().trim().isEmpty()) {
                alertFieldsNotCompeleted("Description");
            } else if (dateTextView.getText().toString().trim().isEmpty() && timeWrapper.isDateDifferenceNegative() == 100) {
                alertFieldsNotCompeleted("Date");
            }else if( timeWrapper.isDateDifferenceNegative() == -100){
                alertFieldsNotCompeleted("Date");
        }else{
               AsyncTask.execute(() -> {
                   db.toDoDao().insert(newModel);
                   Intent i = new Intent(getContext(), MainScreen.class);
                   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   getContext().startActivity(i);
               });
                isInsertedModel = true;
            }

            AsyncTask.execute(() -> {
                List<ToDoModel> modelList = db.toDoDao().getAllModelsNormal();
                for (int i = 0; i <modelList.size() ; i++) {
                    Log.i("aaab", String.valueOf(modelList.get(i).getmId()));
                }
                 RxQuery rxQuery = new RxQuery(getContext(),selectedDateNotifications,notifications);
                //    rxQuery.sendExpireNotification();
            if(selectedDateNotifications.finalTimeInMilli() != -1){
               rxQuery.testPeriodicNotif();
            }
            });





            if(isInsertedModel){
                isInsertedModel = false;

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        List<ToDoModel> modelList = db.toDoDao().getAllModelsNormal();
                        for (int i = 0; i <modelList.size() ; i++) {
                            Log.i("aaaa", String.valueOf(modelList.get(i).ismPause()));
                        }
                    }
                });
            }
        });
        return v;
    }

    public void alertFieldsNotCompeleted(String typeOfField){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Empty Field").setMessage(typeOfField+ " is empty. Please fill it!")
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    //Daca fragmentul apartine activitatii MainScreen, ascundem butonul la onDetach()
    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() instanceof MainScreen) {
            MainScreen mainScreen = (MainScreen) getActivity();
            Objects.requireNonNull(mainScreen.getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CAMERA: {
                //noinspection StatementWithEmptyBody
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    mPhoto = takePic.getmCurrentPhotoPath();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getContext(), "Onactivityserult", Toast.LENGTH_SHORT).show();
        if (requestCode ==  TakePic.TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Glide.with(this).load(takePic.getmCurrentPhotoPath()).into(photo);
            mPhoto = takePic.getmCurrentPhotoPath();
        }
    }
}
