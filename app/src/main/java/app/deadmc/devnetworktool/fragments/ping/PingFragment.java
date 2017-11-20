package app.deadmc.devnetworktool.fragments.ping;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.adapters.PingPagerAdapter;
import app.deadmc.devnetworktool.constants.DevConsts;
import app.deadmc.devnetworktool.fragments.ParentFragment;
import app.deadmc.devnetworktool.helpers.SystemHelper;
import app.deadmc.devnetworktool.modules.PingStructure;
import app.deadmc.devnetworktool.shared_preferences.DevPreferences;

/**
 * Created by Feren on 11.12.2016.
 */
public class PingFragment extends ParentFragment {

    private String currentUrl;
    private Button buttonStart;
    private PingPagerAdapter pingPagerAdapter;
    private ViewPager viewPager;
    private ArrayList<PingStructure> pingStructureArrayList;
    private Thread pingThread;
    private volatile boolean working = false;
    private boolean scrolling = false;
    private boolean needReinstantiate = false;


    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_ping, container, false);
        restoreState(savedInstanceState);
        initElements();

        return myFragmentView;
    }

    @Override
    public void initElements() {
        typeOfFragment = DevConsts.PING;
        super.initElements();
        initViewPager();
        initButton();
    }

    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of loginFragment");
        working = false;
        if (pingThread != null && pingThread.isAlive())
            pingThread.interrupt();
        super.onPause();
    }

    private void initButton() {
        buttonStart = (Button) myFragmentView.findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (working) {
                    buttonStart.setText(getString(R.string.start));
                    working = false;
                } else {
                    buttonStart.setText(getString(R.string.stop));
                    getPings();
                }
            }
        });

        if (working == true) {
            getPings();
            buttonStart.setText(getString(R.string.stop));
            //getPings();
        }
    }

    public void initViewPager() {
        if (pingStructureArrayList != null) {
            //Log.e("pingStructureArrayList","pingStructureArrayList size "+pingStructureArrayList.size());
        } else {
            pingStructureArrayList = new ArrayList<>();
        }
        viewPager = (ViewPager) myFragmentView.findViewById(R.id.viewpager);
        pingPagerAdapter = new PingPagerAdapter(getFragmentManager(),getActivity(),pingStructureArrayList,currentUrl);
        viewPager.setAdapter(pingPagerAdapter);
        TabLayout tabLayout = (TabLayout) myFragmentView.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.e("scrolled","start "+positionOffset);
                if (positionOffset > 0.0f)
                    scrolling = true;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(ViewPager.SCROLL_STATE_IDLE == state){
                    scrolling = false;
                    Log.e("StateChanged","scroll finished");
                    refreshFragments();
                }
            }
        };

        refreshFragments();

        viewPager.addOnPageChangeListener(viewPagerListener);
    }

    private void refreshFragments() {
        Log.e("refreshFragments","in main fragment");

        SparseArray<BasePingFragment> sparseArrayFragments = pingPagerAdapter.getRegisteredFragment();

        for(int i = 0; i < sparseArrayFragments.size(); i++) {
            int key = sparseArrayFragments.keyAt(i);
            BasePingFragment basePingFragment = sparseArrayFragments.get(key);
            basePingFragment.refreshFragment(pingStructureArrayList);
        }
    }

    private void getPings() {
        //Log.e("getPings", "started");
        working = true;
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            private long startTime = System.currentTimeMillis();

            public void run() {

                while (working) {
                    try {
                        Thread.sleep(DevPreferences.getPingDelay());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            try {
                                addMessageToPagerAdapter(SystemHelper.getPing(currentUrl));
                            } catch (IllegalStateException e) {}
                            //Log.e("ping",SystemHelper.executeCmd("ping -c 1 -w 1 google.com", false));
                        }
                    });
                }
                Log.e("thread","stopped");
            }
        };
        pingThread = new Thread(runnable);
        pingThread.start();
    }

    private void addMessageToPagerAdapter(String message) {
        Log.e("thread","addMessageToPagerAdapter");
        PingStructure pingStructure = new PingStructure(message);
        pingStructureArrayList.add(pingStructure);

        int currentItem = viewPager.getCurrentItem();

        SparseArray<BasePingFragment> sparseArrayFragments = pingPagerAdapter.getRegisteredFragment();

        for(int i = 0; i < sparseArrayFragments.size(); i++) {
            int index = sparseArrayFragments.keyAt(i);
            BasePingFragment basePingFragment = sparseArrayFragments.get(index);
            boolean canUpdate = (index == currentItem) && !scrolling;
            basePingFragment.addPingStructure(pingStructure, canUpdate);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.e("onSaveInstanceState", "save");
        outState.putString("pingStructureList", new Gson().toJson(pingStructureArrayList));
        outState.putString("currentUrl", currentUrl);
        outState.putBoolean("working", working);
        working = false;
    }


    private void restoreState(Bundle bundle) {
        //Log.e("restoreState","restore");
        if (bundle==null)
            return;
        if (!bundle.containsKey("pingStructureList"))
            return;
        if (!bundle.containsKey("currentUrl"))
            return;
        //Log.e("restoreState","restore is ok");
        currentUrl = bundle.getString("currentUrl");
        Type type = new TypeToken<ArrayList<PingStructure>>(){}.getType();
        String serializedString = bundle.getString("pingStructureList");
        Gson gson = new Gson();
        pingStructureArrayList = gson.fromJson(serializedString, type);

        if (!bundle.containsKey("working"))
            return;
        working = bundle.getBoolean("working");

    }

    @Override
    public void onDestroyView() {
        if (pingThread != null && pingThread.isAlive())
            pingThread.interrupt();
        super.onDestroyView();

    }

}
