package com.example.ionut.whattodo.fragments.splittedRecyler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.ionut.whattodo.R;
import com.example.ionut.whattodo.database.ToDoModel;
import com.example.ionut.whattodo.fragments.fragmentePrelucrare.DetailFragment;
//import com.example.ionut.whattodo.fragments.fragmentePrelucrare.EditFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;





public  class MultyViewArray extends RecyclerView.Adapter  {



    private final List<ToDoModel> toDoModels;
    private static final int MODEL_PICTURE = 0;
    private static final int MODEL_NORMAL = 1;
    private static final String DETAIL_FRAG_TAG = "detailFragment";



    private TextView mTodo, mDate, mTodoF,mDateF;
    private ImageView photo;
    private Button mIsDone;
    private final Context context;
    private Button mIsDoneF;



    public MultyViewArray(List<ToDoModel> toDoModels, Context context) {
        this.toDoModels = toDoModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i) {
            case MODEL_NORMAL:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main_fragment, viewGroup, false);
                return new ToDoWithoutPic(view);
            case MODEL_PICTURE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main_fragment_photo, viewGroup, false);
                return new ToDoWithPic(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ToDoWithoutPic) {
            final RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder)viewHolder;
            ((ToDoWithoutPic) viewHolder).bindWitouhtImage(toDoModels.get(position));
            viewHolder.setIsRecyclable(false);
            viewHolder.itemView.setOnLongClickListener(view -> {
                AppCompatActivity activity = ((AppCompatActivity)view.getContext());
              //  Fragment fragment = EditFragment.newInstance(toDoModels.get(position).getmName(),toDoModels.get(position).getmDate(),null,toDoModels.get(position).getmId());
               // activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment,  DETAIL_FRAG_TAG)
              //          .addToBackStack(null).commit();
                return true;
            });
            viewHolder.itemView.setOnClickListener(view -> {
                view.setOnClickListener(view1 -> {
                    AppCompatActivity activity = ((AppCompatActivity) view1.getContext());
                    Fragment fragment = DetailFragment.newInstance();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment,  DETAIL_FRAG_TAG)
                            .addToBackStack(null).commit();

                });
                if (toDoModels.get(position).ismDone()) {
                    mIsDone.setText(String.valueOf(true));
                }
            });
        } else if (viewHolder instanceof ToDoWithPic) {
            ((ToDoWithPic) viewHolder).bindWithImage(toDoModels.get(position));
            viewHolder.setIsRecyclable(false);
            viewHolder.itemView.setOnClickListener(view -> {
                AppCompatActivity activity = ((AppCompatActivity)view.getContext());
                Fragment fragment = DetailFragment.newInstance();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment,DETAIL_FRAG_TAG)
                        .addToBackStack(null).commit();
            });
            viewHolder.itemView.setOnLongClickListener(view -> {
                AppCompatActivity activity = ((AppCompatActivity)view.getContext());
             //   Fragment fragment = EditFragment.newInstance(toDoModels.get(position).getmName(),toDoModels.get(position).getmDate(),toDoModels.get(position).getmPath(),toDoModels.get(position).getmId());
             //   activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment,  DETAIL_FRAG_TAG)
               //         .addToBackStack(null).commit();
                return true;
            });
            if (toDoModels.get(position).ismDone()) {
                mIsDone.setText(String.valueOf(true));
            }

        }
    }

    @Override
    public int getItemCount() {
        return toDoModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(toDoModels.get(position).mPath != null){
            return MODEL_PICTURE;
        }else {
            return MODEL_NORMAL;
        }
    }
    private class ToDoWithoutPic extends RecyclerView.ViewHolder{

        private ToDoWithoutPic(@NonNull View itemView) {
            super(itemView);
            mTodo = itemView.findViewById(R.id.todoInfo);
       //     mDate = itemView.findViewById(R.id.dateInfo);
     //       mIsDone = itemView.findViewById(R.id.isDone);
        }

        private void bindWitouhtImage(ToDoModel toDoModel){
            mTodo.setText(toDoModel.getmName());
            mDate.setText(formatDate(toDoModel.getmDate()));
            mIsDone.setText(String.valueOf(toDoModel.ismDone()));
        }

    }




    private class ToDoWithPic extends RecyclerView.ViewHolder{

        private ToDoWithPic(@NonNull View itemView) {
            super(itemView);
            mTodoF = itemView.findViewById(R.id.todoInfoF);
            mDateF = itemView.findViewById(R.id.dateInfoF);
            photo = itemView.findViewById(R.id.photo);
            mIsDoneF = itemView.findViewById(R.id.isDoneF);
        }
        private void bindWithImage(ToDoModel toDoModel){
            mTodoF.setText(( toDoModel.getmName()));
            mDateF.setText(formatDate((( toDoModel.getmDate()))));
            mIsDoneF.setText(String.valueOf(toDoModel.ismDone()));
            Glide.with(Objects.requireNonNull(context)).load(toDoModel.getmPath()).into(photo);
        }

    }
    private String formatDate(Date date){
        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        return formatter.format(date);
    }


}
