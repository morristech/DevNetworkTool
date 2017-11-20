package app.deadmc.devnetworktool.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.deadmc.devnetworktool.R;

/**
 * Created by DEADMC on 6/7/2017.
 */

public class SettingsFragment extends ParentFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView =  inflater.inflate(R.layout.fragment_parent, container, false);
        return myFragmentView;
    }


}
