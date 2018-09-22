package com.example.ionut.whattodo.widgets;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Toast;

import com.example.ionut.whattodo.MainScreen;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class TakePic  implements View.OnClickListener{
    private String mCurrentPhotoPath;
    private Context context;
    private final Fragment parentFragment;
    public static final int TAKE_PHOTO = 1;
    private final FloatingActionButton takePic;
    private boolean isClicked = false;

    public String getmCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public TakePic( final FloatingActionButton button, final Fragment fragment){
   //    this.context = context;
        this.takePic = button;
        this.parentFragment = fragment;

        this.takePic.setClickable(true);
        this.takePic.setOnClickListener(this);
        dispatchTakePictureIntent();

    }


    private void dispatchTakePictureIntent(){
        if(isClicked) {
            if (ContextCompat.checkSelfPermission(takePic.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || (ContextCompat.checkSelfPermission(Objects.requireNonNull(parentFragment.getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED)) {
//                Toast.makeText(context, "Please grant permission", Toast.LENGTH_SHORT).show();

                if (ActivityCompat.shouldShowRequestPermissionRationale(parentFragment.getActivity(),
                        Manifest.permission.CAMERA)) {

                } else {
                    ActivityCompat.requestPermissions(parentFragment.getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            } else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra("return-data", true);
                if (takePictureIntent.resolveActivity(Objects.requireNonNull(parentFragment.getContext()).getPackageManager()) != null) {
                    File photoFile = null;

                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    if (photoFile != null) {
                        Uri photoUri = FileProvider.getUriForFile(context, "com.example.ionut.whattodo", photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        if (parentFragment.getContext() instanceof MainScreen) {
                            parentFragment.startActivityForResult(takePictureIntent, TAKE_PHOTO);
                            Toast.makeText(parentFragment.getContext(), "take photo success", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
        isClicked = false;
    }

    private File createImageFile() throws IOException {
       // mCurrentPhotoPath = null;
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Objects.requireNonNull(parentFragment.getContext()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;

    }

    @Override
    public void onClick(View v) {
        if(!isClicked) {
            isClicked = true;
            dispatchTakePictureIntent();
        }
    }



}