package com.example.ionut.whattodo.fragments.fragmentePrelucrare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ionut.whattodo.MainScreen;
import com.example.ionut.whattodo.Notifications;
import com.example.ionut.whattodo.R;
import com.example.ionut.whattodo.database.ToDoDatabase;
import com.example.ionut.whattodo.widgets.TakePic;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import io.reactivex.schedulers.Schedulers;

public class EditFragment  extends Fragment{

    private static final int TAKE_PICTURE_INTENT = 100;
    private EditText toDoEdit;
    private TextView dateTextView;

    private int mYear;
    private int mMonth;
    private int mDay;
    private Date finalDate ;
    private Date lastInstanceDate;
    private String finalPath = null;
    private String tempPath = null;
    private ImageView imageView;
    private Notifications notifications;
    private Button save;
    private FloatingActionButton picture;
    private TextView description;


    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.single_item_view, container, false);
        save = v.findViewById(R.id.save);
        assert getArguments() != null;
        String toDoText = getArguments().getString("todo");
        long date = getArguments().getLong("date");
        final String[] path = {getArguments().getString("path")};
        tempPath = path[0];
        int id = getArguments().getInt("id");
        lastInstanceDate = new Date();
        picture = v.findViewById(R.id.photo_button);
        lastInstanceDate.setTime(date);
        finalPath = path[0];
        description = v.findViewById(R.id.description);
        dateTextView = v.findViewById(R.id.date);
        imageView = v.findViewById(R.id.photo);


        Date current = new Date();
        current.setTime(date);
       // TimeWrapper timeWrapper = new TimeWrapper(,dateTextView);
      //  String dateById = timeWrapper.getDateFormatted(current);
       // dateTextView.setText(dateById);

        //testing
        final ToDoDatabase db = ToDoDatabase.getInstance(getContext());

        description.setText(toDoText);
        dateTextView.setVisibility(View.VISIBLE);

        Glide.with(Objects.requireNonNull(getContext())).load(tempPath).into(imageView);
            save.setOnClickListener(view -> {
                if (!toDoEdit.getText().toString().equals(String.valueOf(toDoText)) ||
                        !(finalDate == lastInstanceDate) || !(tempPath.equals(finalPath)) ) {
                    db.toDoDao().getItemById(id)
                            .subscribeOn(Schedulers.computation())
                            .subscribe(s -> {
                                if(finalDate != null && finalDate.getTime() > lastInstanceDate.getTime()) {
                                    int uniqueInt = ThreadLocalRandom.current().nextInt(1, 10000000 + 1);
                                    db.toDoDao().updateAllById(id, toDoEdit.getText().toString(), finalDate.getTime(), finalPath);
                                    db.toDoDao().updateById(id,false);
                                    notifications.sendNotDoneNotification(s.getmName(),String.valueOf(s.getmId()),getContext(),finalDate,uniqueInt);
                                }else {
                                    db.toDoDao().updateAllById(id, toDoEdit.getText().toString(), lastInstanceDate.getTime(), finalPath);
                                }

                   });
                    Intent i = new Intent(getContext(), MainScreen.class);
                    startActivity(i);

                }else {
                    Toast.makeText(getContext(), "Nicio modificare", Toast.LENGTH_SHORT).show();
                }
            });
            picture.setOnClickListener(view -> {
                    TakePic takePic;
                    takePic = new TakePic(picture,this);
                    finalPath = takePic.getmCurrentPhotoPath();
                });
        return v;
        }

    public static EditFragment newInstance(String todo, Date date,String photoPath, int id){
        EditFragment newFrag = new EditFragment();
        Bundle bundle = new Bundle();
        bundle.putString("todo",todo);
        bundle.putLong("date",date.getTime());
        bundle.putString("path",photoPath);
        bundle.putInt("id",id);
        newFrag.setArguments(bundle);
        return newFrag;
    }
    private String fromDatetoString(long dateLong){
        Date date = new Date();
        date.setTime(dateLong);
        @SuppressLint("SimpleDateFormat") Format format  = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        return format.format(date);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EditFragment.TAKE_PICTURE_INTENT && resultCode == Activity.RESULT_OK) {
            Glide.with(this).load(finalPath).into(imageView);
        }else {
            Glide.with(this).load(tempPath).into(imageView);
        }

    }

    @Override
    public void onPause() {
        AppCompatActivity activity = ((AppCompatActivity)getContext());
        activity.getSupportFragmentManager().beginTransaction().remove(EditFragment.this).commit();
        super.onPause();
    }
}
