package app.deadmc.devnetworktool.ui.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.ui.presentation.fragments.rest.RestRequestFragment
import app.deadmc.devnetworktool.ui.presentation.fragments.rest.RestResponseFragment
import app.deadmc.devnetworktool.ui.presentation.fragments.rest.RestHistoryFragment

class RestPagerAdapter(fragmentManager: FragmentManager,
                       private val context: Context) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return RestRequestFragment()
            }
            1 -> {
                return RestResponseFragment()
            }
            2 -> {
                return RestHistoryFragment()
            }
        }

        return null
    }


    override fun getCount(): Int {
        return NUM_ITEMS
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.getString(PAGE_NAMES[position])
    }

    companion object {
        private val NUM_ITEMS = 3
        private val PAGE_NAMES = intArrayOf(R.string.request, R.string.response, R.string.history)
    }


}