package com.example.ionut.whattodo.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ionut.whattodo.MainScreen;
import com.example.ionut.whattodo.Notifications;
import com.example.ionut.whattodo.R;
import com.example.ionut.whattodo.database.ToDoModel;
import com.example.ionut.whattodo.helpers.DatabaseQueries;
import com.example.ionut.whattodo.widgets.SelectedDateNotifications;
import com.example.ionut.whattodo.widgets.TakePic;
import com.example.ionut.whattodo.widgets.TimeWrapper;

import java.util.Objects;

//Implementam interfata din care luam date de la alt fragment
public class FragmentAddItem extends Fragment  {

    private static final int PERMISSION_CAMERA = 1;


    private TextInputEditText description;
    private TakePic takePic;
    private String mPhoto;
    private ImageView photo;
    private ImageView calendarImageView;


    private TimeWrapper timeWrapper;
    private TextView dateTextView;




    private SelectedDateNotifications selectedDateNotifications;


    @SuppressLint({"CheckResult", "CutPasteId"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.single_item_view, container, false);
        LinearLayout linearLayout = v.findViewById(R.id.notif_layout);

        Button mSave = v.findViewById(R.id.save);
        description = v.findViewById(R.id.description);

        TextInputEditText daysSelect = v.findViewById(R.id.textDays);


        TextInputEditText hoursSelect = v.findViewById(R.id.textHours);
        TextInputEditText minutesSelect = v.findViewById(R.id.textMinutes);
        TextView date1 = v.findViewById(R.id.date);


         selectedDateNotifications = new SelectedDateNotifications(getContext());
         selectedDateNotifications.setDaysInput(daysSelect);
         selectedDateNotifications.setHoursInput(hoursSelect);
         selectedDateNotifications.setMinutesInput(minutesSelect);


         dateTextView = v.findViewById(R.id.dateTextView);


        FloatingActionButton photo_button = v.findViewById(R.id.photo_button);

        photo = v.findViewById(R.id.photo);


        takePic = new TakePic( photo_button,this);
         timeWrapper = new TimeWrapper(calendarImageView, date1,dateTextView,getContext(),selectedDateNotifications,linearLayout);



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

        //Introduce in baza de date
        mSave.setOnClickListener((View view) -> {
           dbInsert();
        });
        return v;
    }

    private void alertFieldsNotCompeleted(String typeOfField){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Empty Field").setMessage(typeOfField+ " is empty. Please fill it!")
                .setNegativeButton("Dismiss", (dialog, which) -> dialog.dismiss()).show();

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
            photo.setVisibility(View.VISIBLE);
            mPhoto = takePic.getmCurrentPhotoPath();
        }
    }

    public void dbInsert(){
        Notifications notifications = new Notifications(getContext());
        DatabaseQueries queries = new DatabaseQueries(getContext(),selectedDateNotifications,notifications);
        ToDoModel newModel = new ToDoModel(Objects.requireNonNull(description.getText()).toString().trim(),timeWrapper.getCurrentDate(), false, mPhoto,false,selectedDateNotifications.finalTimeInMilli());
        if (description.getText().toString().trim().isEmpty()) {
            alertFieldsNotCompeleted("Description");
        } else if (dateTextView.getText().toString().trim().isEmpty() && timeWrapper.isDateDifferenceNegative() == 100) {
            alertFieldsNotCompeleted("Date");
        }else if( timeWrapper.isDateDifferenceNegative() == -100){
            alertFieldsNotCompeleted("Date");
        }else if(selectedDateNotifications.finalTimeInMilli() == -1){
            Toast.makeText(getContext(), "Timpul notificarilor e mai mare decat timpul final", Toast.LENGTH_SHORT).show();
        }else if(selectedDateNotifications.finalTimeInMilli() == 10) {
            Toast.makeText(getContext(), "Nu se poate pute o notificare mai mica de 15 minute", Toast.LENGTH_SHORT).show();
        }else{
            queries.insertNewModel(newModel);
            queries.sendExpireNotification();
        }
        AsyncTask.execute(() -> {
            if(selectedDateNotifications.finalTimeInMilli() != -1 && selectedDateNotifications.finalTimeInMilli() !=0){
                queries.periodicNotif();
            }else{
                //Nu avem notificare periodica
            }
        });
    }
}
