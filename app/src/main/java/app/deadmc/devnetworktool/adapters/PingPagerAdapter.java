package app.deadmc.devnetworktool.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.fragments.ping.BasePingFragment;
import app.deadmc.devnetworktool.fragments.ping.PingChartPageFragment;
import app.deadmc.devnetworktool.fragments.ping.PingRawPageFragment;
import app.deadmc.devnetworktool.fragments.ping.PingStatsPageFragment;
import app.deadmc.devnetworktool.modules.PingStructure;
import app.deadmc.devnetworktool.presenters.PingPresenter;

/**
 * Created by Feren on 11.12.2016.
 */
public class PingPagerAdapter extends FragmentStatePagerAdapter {

    private SparseArray<BasePingFragment> registeredFragments = new SparseArray<>();
    private String currentUrl;
    private PingPresenter pingPresenter;

    private static int NUM_ITEMS = 3;
    private static int[] PAGE_NAMES = {R.string.raw_data,R.string.chart,R.string.stats};

    private Context context;

    public PingPagerAdapter(FragmentManager fragmentManager,
                            Context context,
                            PingPresenter pingPresenter) {
        super(fragmentManager);
        this.context = context;
        this.pingPresenter = pingPresenter;
    }

    @Override
    public Fragment getItem(int position) {
        BasePingFragment basePingFragment = null;
        switch (position) {
            case 0:
                basePingFragment = new PingRawPageFragment();
                break;
            case 1:
                basePingFragment = new PingChartPageFragment();
                break;
            case 2:
                basePingFragment = new PingStatsPageFragment();
                PingStatsPageFragment pingStatsPageFragment = (PingStatsPageFragment) basePingFragment;
                pingStatsPageFragment.setCurrentUrl(pingPresenter.getCurrentUrl());
                break;
        }
        //if (basePingFragment != null)
           // basePingFragment.setPingPresenter(pingPresenter);
        return  basePingFragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.e("instantiateItem","position "+position);
        BasePingFragment fragment = (BasePingFragment) super.instantiateItem(container, position);
        try {
            registeredFragments.append(position,fragment);
        } catch (IndexOutOfBoundsException e){
            Log.e("instantiateItem",Log.getStackTraceString(e));
        }

        //fragment.setPingPresenter(pingPresenter);
        //pingPresenter.getPingPagePresenterList().add(fragment.getPresenter());
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        try {
            registeredFragments.remove(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        super.destroyItem(container, position, object);
    }

    public SparseArray<BasePingFragment> getRegisteredFragment() {
        return registeredFragments;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(PAGE_NAMES[position]);
    }





}