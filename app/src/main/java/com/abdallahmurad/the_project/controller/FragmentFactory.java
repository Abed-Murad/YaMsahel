package com.abdallahmurad.the_project.controller;

import android.graphics.drawable.shapes.Shape;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v4.app.Fragment;

import com.abdallahmurad.the_project.view.fragments.ChatFragment;
import com.abdallahmurad.the_project.view.fragments.CitiesFragment;
import com.abdallahmurad.the_project.view.fragments.ProfileFragment;
import com.google.android.gms.maps.model.Circle;

/**
 * Created by Abdallah Murad on 12/9/2017.
 */

public class FragmentFactory {
    public Fragment getFragment(String fragmentType){
        if(fragmentType == null){
            return null;
        }
        if(fragmentType.equalsIgnoreCase("CitiesFragment")){
            return new CitiesFragment();

        } else if(fragmentType.equalsIgnoreCase("ProfileFragment")){
            return new ProfileFragment();

        } else if(fragmentType.equalsIgnoreCase("ChatFragment")){
            return new ChatFragment();
        }

        return null;
    }
}
