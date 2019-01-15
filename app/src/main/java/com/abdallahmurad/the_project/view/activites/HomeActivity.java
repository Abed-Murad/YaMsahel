package com.abdallahmurad.the_project.view.activites;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.abdallahmurad.the_project.controller.FragmentFactory;
import com.abdallahmurad.the_project.view.CallDialog;
import com.abdallahmurad.the_project.view.fragments.ChatFragment;
import com.abdallahmurad.the_project.view.fragments.CitiesFragment;
import com.abdallahmurad.the_project.view.fragments.ProfileFragment;
import com.abdallahmurad.the_project.R;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;

import static com.abdallahmurad.the_project.controller.FragmentUtils.addFragment;
import static com.abdallahmurad.the_project.controller.FragmentUtils.replaceFragment;

public class HomeActivity extends AppCompatActivity {
    CallDialog callDialog;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addFragment(this, R.id.fragment_container, false, new CitiesFragment());
        callDialog = new CallDialog();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        BottomNavigationItem homeItem = new BottomNavigationItem("Home", ContextCompat.getColor(this, R.color.BottomNavItem), R.drawable.ic_home_black_24dp);
        BottomNavigationItem profileItem = new BottomNavigationItem("Profile", ContextCompat.getColor(this, R.color.BottomNavItem), R.drawable.ic_person);
        BottomNavigationItem chatItem = new BottomNavigationItem("Chat", ContextCompat.getColor(this, R.color.BottomNavItem), R.drawable.ic_chat);
        BottomNavigationItem communityItem = new BottomNavigationItem("Community", ContextCompat.getColor(this, R.color.BottomNavItem), R.drawable.ic_community_black_24dp);
        bottomNavigationView.addTab(homeItem);
        bottomNavigationView.addTab(communityItem);
        bottomNavigationView.addTab(profileItem);
        bottomNavigationView.addTab(chatItem);
        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                FragmentFactory fragmentFactory = new FragmentFactory();
                Fragment fragment;
                switch (index) {
                    case 0:
                         fragment = fragmentFactory.getFragment("CitiesFragment");
                        replaceFragment(HomeActivity.this, R.id.fragment_container, false, fragment);
                        break;
                    case 1:
                        startActivity(new Intent(HomeActivity.this , CommMainActivity.class));
                        break;
                    case 2:
                        fragment = fragmentFactory.getFragment("ProfileFragment");
                        replaceFragment(HomeActivity.this, R.id.fragment_container, false, fragment);

                        break;
                    case 3:
                        fragment = fragmentFactory.getFragment("ChatFragment");
                        replaceFragment(HomeActivity.this, R.id.fragment_container, false, fragment);


                        break;
                }
            }
        });
    }
 }



