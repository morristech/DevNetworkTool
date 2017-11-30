package app.deadmc.devnetworktool.fragments.rest

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.adapters.RestPagerAdapter
import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.fragments.ping.MainPingFragment
import app.deadmc.devnetworktool.interfaces.RestView
import app.deadmc.devnetworktool.modules.ConnectionHistory
import app.deadmc.devnetworktool.modules.ResponseDev
import app.deadmc.devnetworktool.presenters.RestPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_rest.view.*
import java.io.Serializable


class MainRestFragment : BaseFragment(),RestView {
    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var restPresenter: RestPresenter
    private lateinit var restPagerAdapter: RestPagerAdapter

    companion object {
        fun getInstance(serializable: Serializable): MainRestFragment {
            val fragment = MainRestFragment()
            val args = Bundle()
            args.putSerializable("connection_history", serializable)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentConnectionHistory = this.arguments.getSerializable("connection_history") as ConnectionHistory
        restPresenter.currentUrl = currentConnectionHistory.ipAddress
        Log.e(TAG,"currentUrl = "+currentConnectionHistory.ipAddress)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater!!.inflate(R.layout.fragment_rest, container, false)
        initViewPager()
        return myFragmentView
    }

    @ProvidePresenter(type = PresenterType.LOCAL)
    fun providePresenter(): RestPresenter {
        return RestPresenter()
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
                restPresenter.currentPage = position
            }
        })
        myFragmentView.viewPager.currentItem = restPresenter.currentPage
    }

    override fun setResponse(responseDev: ResponseDev) {

    }

}
