package app.deadmc.devnetworktool.fragments.rest

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.adapters.RestPagerAdapter
import app.deadmc.devnetworktool.events.PageChangedEvent
import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.interfaces.views.RestView
import app.deadmc.devnetworktool.models.ResponseDev
import app.deadmc.devnetworktool.models.RestRequestHistory
import app.deadmc.devnetworktool.observables.RxBus
import kotlinx.android.synthetic.main.fragment_ping.*
import kotlinx.android.synthetic.main.fragment_rest.view.*
import kotlinx.android.synthetic.main.horizontal_progress_bar.view.*


class MainRestFragment : BaseFragment(), RestView {
    private lateinit var restPagerAdapter: RestPagerAdapter


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater!!.inflate(R.layout.fragment_rest, container, false)
        initViewPager()
        hideProgress()
        return myFragmentView
    }

    override fun onResume() {
        super.onResume()
        activity.setTitle(R.string.rest_client)
    }

    override fun showProgress() {
        myFragmentView.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        myFragmentView.progressBar.visibility = View.INVISIBLE
    }

    private fun initViewPager() {
        restPagerAdapter = RestPagerAdapter(fragmentManager, activity)
        myFragmentView.viewPager.adapter = restPagerAdapter
        myFragmentView.viewPager.offscreenPageLimit = 5
        myFragmentView.tabLayout.setupWithViewPager(myFragmentView.viewPager)
        myFragmentView.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                //restPresenter.currentPage = position
                RxBus.post(PageChangedEvent(position))
            }
        })
        //myFragmentView.viewPager.currentItem = restPresenter.currentPage
    }

    fun setResponse(responseDev: ResponseDev) {
        viewPager.setCurrentItem(1, true)
    }

    override fun loadRequestHistory(restRequestHistory: RestRequestHistory) {
        viewPager.setCurrentItem(0, true)
    }
}
