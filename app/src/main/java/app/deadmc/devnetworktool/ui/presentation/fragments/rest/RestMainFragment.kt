package app.deadmc.devnetworktool.ui.presentation.fragments.rest

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.ui.adapters.RestPagerAdapter
import app.deadmc.devnetworktool.ui.presentation.fragments.BaseFragment
import app.deadmc.devnetworktool.ui.presentation.views.RestMainView
import app.deadmc.devnetworktool.ui.presentation.presenters.BasePresenter
import app.deadmc.devnetworktool.ui.presentation.presenters.RestMainPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_rest.view.*
import kotlinx.android.synthetic.main.horizontal_progress_bar.view.*


class RestMainFragment : BaseFragment(), RestMainView {

    @InjectPresenter
    lateinit var mainRestPresenter: RestMainPresenter
    lateinit var restPagerAdapter: RestPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater.inflate(R.layout.fragment_rest, container, false)
        initViewPager()
        hideProgress()
        return myFragmentView
    }

    override fun getPresenter(): BasePresenter<*> {
        return mainRestPresenter
    }

    override fun onResume() {
        super.onResume()
        activity?.setTitle(R.string.rest_client)
    }

    override fun showProgress() {
        myFragmentView.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        myFragmentView.progressBar.visibility = View.INVISIBLE
    }

    private fun initViewPager() {
        restPagerAdapter = RestPagerAdapter(fragmentManager!!, activity!!)
        myFragmentView.viewPager.adapter = restPagerAdapter
        myFragmentView.viewPager.offscreenPageLimit = 3
        myFragmentView.viewPager.swipeEnabled = false
        myFragmentView.tabLayout.setupWithViewPager(myFragmentView.viewPager)
        myFragmentView.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                mainRestPresenter.currentPage = position
                myFragmentView.viewPager.swipeEnabled = position == 1
            }
        })
        myFragmentView.viewPager.currentItem = mainRestPresenter.currentPage
    }

    override fun slideViewPager(position: Int) {
        myFragmentView.viewPager.setCurrentItem(position, true)
    }
}
