package com.abdallahmurad.the_project.view.activites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.abdallahmurad.the_project.view.fragments.FilterFragment;
import com.abdallahmurad.the_project.R;

import static com.abdallahmurad.the_project.controller.FragmentUtils.addFragment;


/**
 * Created by Desmond on 8/21/2017.
 */

public class FiltersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        addFragment(this,R.id.filter_container,false,new FilterFragment());
    }

}
