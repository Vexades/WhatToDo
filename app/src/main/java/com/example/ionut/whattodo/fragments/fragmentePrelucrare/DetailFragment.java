package com.example.ionut.whattodo.fragments.fragmentePrelucrare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ionut.whattodo.MainScreen;
import com.example.ionut.whattodo.R;
import com.example.ionut.whattodo.database.ToDoDatabase;

import java.util.Objects;

import io.reactivex.schedulers.Schedulers;

public class DetailFragment extends Fragment {

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Daca fragmetnul apartine activitaii MainScreen, arata butonul
        if(getActivity() instanceof MainScreen){
            MainScreen mainScreen = (MainScreen)getActivity();
            Objects.requireNonNull(mainScreen.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        ToDoDatabase db = ToDoDatabase.getInstance(getContext());
//        String path = getArguments().getString("path");
  //      String todo = getArguments().getString("todo");
  //      String date = getArguments().getString("date");
  //      String isDone = getArguments().getString("isDone");
  //      int id = getArguments().getInt("id");
        TextView dateText;
        TextView isDoneText;
        Button delete;
        TextView todoText;
        Button button;

            View v = inflater.inflate(R.layout.detail_layout, container, false);


        return v;

    }
    public static DetailFragment newInstance(){
        DetailFragment newFrag = new DetailFragment();
     //   Bundle bundle = new Bundle();
     //   bundle.putString("todo",todo);
     //   bundle.putString("date",date);
     //   bundle.putString("isDone",isDone);
     //   bundle.putString("path",photoPath);
      //  bundle.putInt("id",id);
      //  newFrag.setArguments(bundle);
        return newFrag;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(getActivity() instanceof MainScreen){
            MainScreen mainScreen = (MainScreen)getActivity();
            Objects.requireNonNull(mainScreen.getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        }
    }
    @SuppressLint("CommitTransaction")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        } else {
           Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().remove(DetailFragment.this);
        }
    }

}
