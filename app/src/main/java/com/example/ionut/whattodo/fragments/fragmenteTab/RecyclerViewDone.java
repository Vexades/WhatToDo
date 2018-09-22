package com.example.ionut.whattodo.fragments.fragmenteTab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ionut.whattodo.R;

import com.example.ionut.whattodo.fragments.splittedRecyler.DbRelated;

public class RecyclerViewDone extends Fragment {
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler,container,false);
         recyclerView = v.findViewById(R.id.recyclerView);
         recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DbRelated dbRelated = new DbRelated(getContext(), recyclerView);
        dbRelated.updateDoneItems();

        return v;
    }
}
