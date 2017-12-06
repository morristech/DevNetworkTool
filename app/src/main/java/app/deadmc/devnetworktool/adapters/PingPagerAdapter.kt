package app.deadmc.devnetworktool.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.fragments.ping.BasePingFragment
import app.deadmc.devnetworktool.fragments.ping.PingChartPageFragment
import app.deadmc.devnetworktool.fragments.ping.PingRawPageFragment
import app.deadmc.devnetworktool.fragments.ping.PingStatsPageFragment

class PingPagerAdapter(fragmentManager: FragmentManager,
                       private val context: Context) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? {
        var basePingFragment: BasePingFragment? = null
        when (position) {
            0 -> basePingFragment = PingRawPageFragment()
            1 -> basePingFragment = PingChartPageFragment()
            2 -> basePingFragment = PingStatsPageFragment()
        }
        return basePingFragment
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return super.instantiateItem(container, position) as BasePingFragment
    }

    override fun getCount(): Int {
        return NUM_ITEMS
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.getString(PAGE_NAMES[position])
    }

    companion object {
        private val NUM_ITEMS = 3
        private val PAGE_NAMES = intArrayOf(R.string.raw_data, R.string.chart, R.string.stats)
    }


}