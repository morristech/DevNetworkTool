package app.deadmc.devnetworktool.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.deadmc.devnetworktool.R;

public class SettingsFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView =  inflater.inflate(R.layout.fragment_settings, container, false);
        return myFragmentView;
    }


}
