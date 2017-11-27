package app.deadmc.devnetworktool.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.constants.DevConsts;

public class ParentFragment extends BaseFragment {

    protected String typeOfFragment = "";
    protected View myFragmentView;
    public ParentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("typeOfFragment")) {
                typeOfFragment = savedInstanceState.getString("typeOfFragment");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();


    }

    public void initElements() {
        registerFragmentInActivity();
        setTitle();
    }

    private void registerFragmentInActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parent, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (typeOfFragment != null) {
            outState.putString("typeOfFragment", typeOfFragment);
        } else {
        }
    }

    public void setTypeOfFragment(String typeOfFragment) {
        this.typeOfFragment = typeOfFragment;
    }

    public void setTitle() {

    }


}
