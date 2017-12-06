package app.deadmc.devnetworktool.models;

import app.deadmc.devnetworktool.R;

/**
 * Created by Feren on 11.12.2016.
 */
public enum PingPagerEnum {

    RAW(R.string.raw_data, R.layout.fragment_pager_recyclerview),
    CHART(R.string.chart, R.layout.fragment_pager_chart),
    STATS(R.string.stats, R.layout.fragment_pager_recyclerview);

    private int mTitleResId;
    private int mLayoutResId;

    PingPagerEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}