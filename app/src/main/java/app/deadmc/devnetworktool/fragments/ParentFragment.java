package app.deadmc.devnetworktool.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.activities.MainActivity2;
import app.deadmc.devnetworktool.constants.DevConsts;

public class ParentFragment extends BaseFragment {

    protected String typeOfFragment = "";
    protected View myFragmentView;
    protected MainActivity2 mainActivity2;

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


        try {
            mainActivity2 = (MainActivity2) getActivity();
        } catch (ClassCastException e) {}


        if (mainActivity2 != null)
            mainActivity2.setCurrentFragment(null);


    }

    public void initElements() {
        registerFragmentInActivity();
        setTitle();
    }

    private void registerFragmentInActivity() {
        try {
            mainActivity2 = (MainActivity2) getActivity();
            Log.e("register",this.getClass().getName());
            mainActivity2.setCurrentFragment(this);
        } catch (ClassCastException e) {}

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

        if (typeOfFragment.isEmpty())
            return;
        switch (typeOfFragment) {
            case DevConsts.TCP_CLIENT:
                mainActivity2.setCustomTitle(R.string.tcp_client);
                break;
            case DevConsts.UDP_CLIENT:
                Log.e("setTitle", typeOfFragment);
                mainActivity2.setCustomTitle(R.string.udp_client);
                break;
            case DevConsts.PING:
                Log.e("setTitle", typeOfFragment);
                mainActivity2.setCustomTitle(R.string.ping);
                break;
            case DevConsts.REST:
                mainActivity2.setCustomTitle(R.string.rest_client);
                break;
            case DevConsts.SETTINGS:
                mainActivity2.setCustomTitle(R.string.action_settings);
                break;
        }
    }


}
