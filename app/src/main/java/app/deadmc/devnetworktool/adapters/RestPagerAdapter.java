package app.deadmc.devnetworktool.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.fragments.ParentFragment;
import app.deadmc.devnetworktool.fragments.rest.RequestRestFragment;
import app.deadmc.devnetworktool.fragments.rest.ResponseRestFragment;
import app.deadmc.devnetworktool.fragments.rest.RestFragment;
import app.deadmc.devnetworktool.fragments.rest.RestHistoryFragment;

/**
 * Created by Feren on 11.12.2016.
 */
public class RestPagerAdapter extends FragmentStatePagerAdapter {

    private SparseArray<ParentFragment> registeredFragments = new SparseArray<>();
    private RestFragment restFragment;
    private String currentUrl;

    private static int NUM_ITEMS = 3;
    private static int[] PAGE_NAMES = {R.string.request,R.string.response, R.string.history};

    private Context context;

    public RestPagerAdapter(FragmentManager fragmentManager,
                            Context context,
                            RestFragment restFragment) {
        super(fragmentManager);
        this.context = context;
        this.restFragment = restFragment;
        this.currentUrl = currentUrl;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RequestRestFragment requestRestFragment = new RequestRestFragment();
                requestRestFragment.setRestFragment(restFragment);
                requestRestFragment.setCurrentUrl(currentUrl);
                return requestRestFragment;
            case 1:
                ResponseRestFragment responseRestFragment = new ResponseRestFragment();
                return responseRestFragment;
            case 2:
                RestHistoryFragment restHistoryFragment = new RestHistoryFragment();
                restHistoryFragment.setRestFragment(restFragment);
                return restHistoryFragment;
        }

        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.e("instantiateItem","position "+position);
        ParentFragment fragment = (ParentFragment) super.instantiateItem(container, position);
        try {
            registeredFragments.append(position,fragment);
        } catch (IndexOutOfBoundsException e){
            Log.e("instantiateItem",Log.getStackTraceString(e));
        }
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

    public SparseArray<ParentFragment> getRegisteredFragment() {
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