package app.deadmc.devnetworktool.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.fragments.rest.RequestRestFragment;
import app.deadmc.devnetworktool.fragments.rest.ResponseRestFragment;
import app.deadmc.devnetworktool.fragments.rest.MainRestFragment;
import app.deadmc.devnetworktool.fragments.rest.RestHistoryFragment;

public class RestPagerAdapter extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 3;
    private static int[] PAGE_NAMES = {R.string.request,R.string.response, R.string.history};

    private Context context;

    public RestPagerAdapter(FragmentManager fragmentManager,
                            Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RequestRestFragment requestRestFragment = new RequestRestFragment();
                return requestRestFragment;
            case 1:
                ResponseRestFragment responseRestFragment = new ResponseRestFragment();
                return responseRestFragment;
            case 2:
                RestHistoryFragment restHistoryFragment = new RestHistoryFragment();
                return restHistoryFragment;
        }

        return null;
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