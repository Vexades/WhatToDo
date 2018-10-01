package com.example.ionut.whattodo.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import com.example.ionut.whattodo.MainScreen;
import com.example.ionut.whattodo.database.ToDoDatabase;
import com.example.ionut.whattodo.database.ToDoModel;
import com.example.ionut.whattodo.helpers.SelectedDateNotificationEnum;
import com.example.ionut.whattodo.managers.BroadCastManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class BaseFragment extends Fragment {
    private String mCurrentPhotoPath;
    public static final int TAKE_PHOTO = 1;
    private final ToDoDatabase db = ToDoDatabase.getInstance(getContext());


    //Pornim intentul care porneste camera
    public void dispatchTakePictureIntent(){
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED)) {
//                Toast.makeText(context, "Please grant permission", Toast.LENGTH_SHORT).show();

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.CAMERA)) {

                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            } else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra("return-data", true);
                if (takePictureIntent.resolveActivity(Objects.requireNonNull(getContext()).getPackageManager()) != null) {
                    File photoFile = null;

                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    if (photoFile != null) {
                        Uri photoUri = FileProvider.getUriForFile(getContext(), "com.example.ionut.whattodo", photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(takePictureIntent, TAKE_PHOTO);
                    }
                }
            }
        }


    private File createImageFile() throws IOException {
        // mCurrentPhotoPath = null;
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Objects.requireNonNull(getContext()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //Ia path-ul pozei pe care o facem dupa ce lansam si facem poza
    public String getmCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void insertNewModel(ToDoModel toDoModel){
        AsyncTask.execute(() -> {
            db.toDoDao().insert(toDoModel);
            Intent i = new Intent(getContext(), MainScreen.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Objects.requireNonNull(getContext()).startActivity(i);

        });
    }

    public void periodicNotif(){
        AsyncTask.execute(() -> {
            List<ToDoModel> modelList = db.toDoDao().getAllModelsNormal();
            ToDoModel b = modelList.get( modelList.size() - 1 );
            sendPeriodicNotification(b.getmName(),b.getmId(), getContext()
                    ,SelectedDateNotificationEnum.INSTANCE.getDate(),b.ismPause());
        });
    }

    public void sendExpireNotification(){
        AsyncTask.execute(() -> {
            int uniqueInt = ThreadLocalRandom.current().nextInt(1, 10000000 + 1);
            List<ToDoModel> modelList = db.toDoDao().getAllModelsNormal();
            ToDoModel b = modelList.get( modelList.size() - 1 );
            sendNotDoneNotification(b.getmName(),b.getmId(),getContext(),b.getmDate(),uniqueInt,1);
        });
    }

    private void sendNotDoneNotification(String name, int idOfModel, Context context, Date finalDate, int uniqueInt, int cancelPeriodic) {
        int uniqueId ;
        if(uniqueInt == 0){
            uniqueId = 100000;
        }else {
            uniqueId = uniqueInt * 100000;
        }
        Intent intent = new Intent(context, BroadCastManager.class);
        intent.putExtra("toDoName", name);
        intent.putExtra("id", idOfModel);
        intent.putExtra("unique", uniqueId);
        intent.putExtra("date", finalDate.getTime());
        intent.putExtra("cancelPeriodic", cancelPeriodic);
        AlarmManager alarm = (AlarmManager) Objects.requireNonNull(context).getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, uniqueInt, intent, PendingIntent.FLAG_ONE_SHOT);
        Objects.requireNonNull(alarm).set(AlarmManager.RTC_WAKEUP, finalDate.getTime(), alarmIntent);
    }

    private void sendPeriodicNotification(String name, int idOfModel, Context context,
                                          long timeOfPeriodicNotif, boolean paused){
        Intent intent = new Intent(context, BroadCastManager.class);
        intent.putExtra("toDoName", name);
        intent.putExtra("id", idOfModel);
        intent.putExtra("unique", idOfModel);
        intent.putExtra("paused",paused);
        intent.putExtra("test",System.currentTimeMillis());
        AlarmManager alarm = (AlarmManager)Objects.requireNonNull(context).getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,idOfModel,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        assert alarm != null;
        alarm.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis()+timeOfPeriodicNotif, timeOfPeriodicNotif, pendingIntent);
    }




}
