package com.example.ionut.whattodo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;

import com.example.ionut.whattodo.fragments.FragmentAddItem;
import com.example.ionut.whattodo.fragments.LockableViewPager;
//import com.example.ionut.whattodo.fragments.fragmentePrelucrare.EditFragment;
import com.example.ionut.whattodo.fragments.fragmenteTab.RecyclerViewDone;
import com.example.ionut.whattodo.fragments.fragmenteTab.RecylerViewNotDone;
import com.example.ionut.whattodo.fragments.ViewPageAdapter;

public class MainScreen extends AppCompatActivity  {


    private static final String ADD_NOTE_FRAGMENT_TAG = "addNoteFragment";
    private FloatingActionButton fab;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment fragment2 = fragmentManager.findFragmentById(R.id.frameLayout);



    @SuppressLint({"RestrictedApi", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_layout);
        fab = findViewById(R.id.floatingActionButton);
        android.support.v7.widget.Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LockableViewPager lockableViewPager;
        lockableViewPager = findViewById(R.id.pager);
        lockableViewPager.setSwipeable(false);
        @SuppressLint("CutPasteId") ViewPager viewPager = findViewById(R.id.pager);
        setupViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }


    //Creem un meniu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    //Creem la o apasare de buton un fragment
    @SuppressLint("RestrictedApi")
    public void changeFragment(View v) {
        Animation anim = android.view.animation.AnimationUtils.loadAnimation(fab.getContext(), R.anim.shake);
        if (fragment2 == null) {
            anim.setDuration(200L);
            fab.startAnimation(anim);
            fragment2 = new FragmentAddItem();
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_left,R.anim.slide_out_left,R.anim.slide_out_right,R.anim.slide_right).add(R.id.frameLayout, fragment2, ADD_NOTE_FRAGMENT_TAG).addToBackStack(null).commit();
        } else {
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_left,R.anim.slide_out_left,R.anim.slide_out_right,R.anim.slide_right).add(R.id.frameLayout, fragment2, ADD_NOTE_FRAGMENT_TAG).addToBackStack(null).commit();
        }
    }

    //Tine in back stack doar o instanta a fragmentului
    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager();
            getSupportFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }else{
            super.onBackPressed();
        }
        }



    private void setupViewPager(ViewPager viewPager) {
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.addFrag(new RecylerViewNotDone(),"In Progress");
        adapter.addFrag(new RecyclerViewDone(),"Expired");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("destory","destoryeed");
        SharedPreferences sharedPref =this.getPreferences(Context.MODE_PRIVATE);
        sharedPref.edit().remove("test").clear().commit();
    }
}



