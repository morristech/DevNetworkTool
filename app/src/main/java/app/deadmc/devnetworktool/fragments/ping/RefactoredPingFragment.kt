package app.deadmc.devnetworktool.fragments.ping

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.adapters.PingPagerAdapter
import app.deadmc.devnetworktool.constants.DevConsts
import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.fragments.socket_connections.WorkingConnectionFragment
import app.deadmc.devnetworktool.interfaces.PingPageView
import app.deadmc.devnetworktool.interfaces.PingView
import app.deadmc.devnetworktool.modules.ConnectionHistory
import app.deadmc.devnetworktool.modules.PingStructure
import app.deadmc.devnetworktool.presenters.PingPagePresenter
import app.deadmc.devnetworktool.presenters.PingPresenter
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.presenter.InjectPresenter
import java.util.ArrayList
import kotlinx.android.synthetic.main.fragment_ping.*
import kotlinx.android.synthetic.main.fragment_ping.view.*
import java.io.Serializable

/**
 * Created by DEADMC on 11/26/2017.
 */
class RefactoredPingFragment : BaseFragment(), PingView {

    @InjectPresenter
    lateinit var pingPresenter:PingPresenter
    lateinit var pingPagerAdapter: PingPagerAdapter


    private var scrolling = false

    companion object {
        fun getInstance(serializable: Serializable): RefactoredPingFragment {
            val fragment = RefactoredPingFragment()
            val args = Bundle()
            args.putSerializable("connection_history", serializable)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentConnectionHistory = this.arguments.getSerializable("connection_history") as ConnectionHistory
        pingPresenter.currentUrl = currentConnectionHistory.ipAddress
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater!!.inflate(R.layout.fragment_ping, container, false)
        initElements()
        return myFragmentView
    }

    fun initElements() {
        initViewPager()
        initButton()
    }

    override fun setStartButtonOn() {
        myFragmentView.startButton.text = getString(R.string.start)
    }

    override fun setStartButtonOff() {
        myFragmentView.startButton.text = getString(R.string.stop)
    }

    private fun initViewPager() {
        pingPagerAdapter = PingPagerAdapter(fragmentManager, activity, pingPresenter)
        myFragmentView.viewPager.adapter = pingPagerAdapter
        myFragmentView.viewPager.offscreenPageLimit = 3
        myFragmentView.tabLayout.setupWithViewPager(myFragmentView.viewPager)

        val viewPagerListener = object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //Log.e("scrolled","start "+positionOffset);
                if (positionOffset > 0.0f)
                    scrolling = true
            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {
                if (ViewPager.SCROLL_STATE_IDLE == state) {
                    scrolling = false
                    Log.e("StateChanged", "scroll finished")
                    //refreshFragments()
                }
            }
        }

        myFragmentView.viewPager.addOnPageChangeListener(viewPagerListener)
    }

    private fun initButton() {
        myFragmentView.startButton.setOnClickListener({
            pingPresenter.handleClick()
        })
    }

}