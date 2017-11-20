package app.deadmc.devnetworktool.fragments.rest;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.adapters.RestPagerAdapter;
import app.deadmc.devnetworktool.constants.DevConsts;
import app.deadmc.devnetworktool.fragments.ObserverFragment;
import app.deadmc.devnetworktool.fragments.ParentFragment;
import app.deadmc.devnetworktool.modules.ResponseDev;
import app.deadmc.devnetworktool.modules.RestRequestHistory;
import app.deadmc.devnetworktool.observables.OkHttpObservable;
import app.deadmc.devnetworktool.singletons.SharedData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

/**
 * Created by Feren on 02.03.2017.
 */
public class RestFragment extends ObserverFragment {

    private String currentUrl;


    private RestPagerAdapter restPagerAdapter;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_rest, container, false);
        restoreState(savedInstanceState);
        initElements();
        return myFragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.e("onSaveInstanceState", "save");
        outState.putString("currentUrl", currentUrl);
    }

    private void restoreState(Bundle bundle) {
        if (bundle==null)
            return;
        if (!bundle.containsKey("currentUrl"))
            return;
        //Log.e("restoreState","restore is ok");
        currentUrl = bundle.getString("currentUrl");

    }

    @Override
    public void initElements() {
        typeOfFragment = DevConsts.REST;
        super.initElements();
        createObserver(Response.class);
        initViewPager();
    }

    public void initViewPager() {
        viewPager = (ViewPager) myFragmentView.findViewById(R.id.viewpager);
        restPagerAdapter = new RestPagerAdapter(getFragmentManager(),getActivity(),currentUrl,this);
        viewPager.setAdapter(restPagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        TabLayout tabLayout = (TabLayout) myFragmentView.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                if (position == 2) {
                    SparseArray<ParentFragment> parentFragmentSparseArray = restPagerAdapter.getRegisteredFragment();
                    for(int i = 0; i < parentFragmentSparseArray.size(); i++) {
                        int key = parentFragmentSparseArray.keyAt(i);
                        ParentFragment parentFragment = parentFragmentSparseArray.get(key);
                        if (parentFragment instanceof  RestHistoryFragment) {
                            RestHistoryFragment restHistoryFragment = (RestHistoryFragment) parentFragment;
                            restHistoryFragment.initElements();
                        }
                    }
                }

            }
        });
    }

    public void sendRequest(final String url, final String requestMethod, final HashMap<String,String> headers, final HashMap<String,String> body) {
        Log.e("sendRequest","url: "+url);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        observable = OkHttpObservable.getObservable(url,requestMethod,headers,body);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(observer);

    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    private void notifyAllNestedFragments(ResponseDev responseDev) {
        SharedData.getInstance().setResponseDev(responseDev);
        SparseArray<ParentFragment> parentFragmentSparseArray = restPagerAdapter.getRegisteredFragment();
        for(int i = 0; i < parentFragmentSparseArray.size(); i++) {
            int key = parentFragmentSparseArray.keyAt(i);
            ParentFragment parentFragment = parentFragmentSparseArray.get(key);
            if (parentFragment instanceof  ResponseRestFragment) {
                try {
                    ResponseRestFragment responseRestFragment = (ResponseRestFragment) parentFragment;
                    responseRestFragment.setResponse(responseDev);
                } catch (Exception e) {}
            }
        }
    }

    public void loadRestHistory(RestRequestHistory restRequestHistory) {
            SparseArray<ParentFragment> parentFragmentSparseArray = restPagerAdapter.getRegisteredFragment();
            for(int i = 0; i < parentFragmentSparseArray.size(); i++) {
                int key = parentFragmentSparseArray.keyAt(i);
                ParentFragment parentFragment = parentFragmentSparseArray.get(key);
                if (parentFragment instanceof  RequestRestFragment) {
                    RequestRestFragment requestRestFragment = (RequestRestFragment) parentFragment;
                    requestRestFragment.setRequestHistory(restRequestHistory);
                }
            }
        viewPager.setCurrentItem(0, true);
    }

    public void setRestFragment() {
        SparseArray<ParentFragment> parentFragmentSparseArray = restPagerAdapter.getRegisteredFragment();
        for(int i = 0; i < parentFragmentSparseArray.size(); i++) {
            int key = parentFragmentSparseArray.keyAt(i);
            ParentFragment parentFragment = parentFragmentSparseArray.get(key);
            if (parentFragment instanceof  RequestRestFragment) {
                RequestRestFragment requestRestFragment = (RequestRestFragment) parentFragment;
                requestRestFragment.setRestFragment(RestFragment.this);
            }

            if (parentFragment instanceof  RestHistoryFragment) {
                RestHistoryFragment requestRestFragment = (RestHistoryFragment) parentFragment;
                requestRestFragment.setRestFragment(RestFragment.this);
            }
        }
    }

    @Override
    protected void handleResult(Object value) {

        ResponseDev responseDev = (ResponseDev) value;
        unsubscribe();
        notifyAllNestedFragments(responseDev);

    }

}
