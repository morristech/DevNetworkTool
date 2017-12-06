package app.deadmc.devnetworktool.fragments.ping

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.adapters.PingPagerAdapter
import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.interfaces.views.PingView
import app.deadmc.devnetworktool.models.ConnectionHistory
import app.deadmc.devnetworktool.models.PingStructure
import app.deadmc.devnetworktool.presenters.PingPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_ping.view.*
import java.io.Serializable
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter

class MainPingFragment : BaseFragment(), PingView {

    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var pingPresenter:PingPresenter
    lateinit var pingPagerAdapter: PingPagerAdapter
    private var scrolling = false

    companion object {
        fun getInstance(serializable: Serializable): MainPingFragment {
            val fragment = MainPingFragment()
            val args = Bundle()
            args.putSerializable("connection_history", serializable)
            fragment.arguments = args
            return fragment
        }
    }

    @ProvidePresenter(type = PresenterType.LOCAL)
    fun providePresenter(): PingPresenter {
        return PingPresenter()
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
        pingPagerAdapter = PingPagerAdapter(fragmentManager, activity)
        myFragmentView.viewPager.adapter = pingPagerAdapter
        myFragmentView.viewPager.offscreenPageLimit = 3
        myFragmentView.tabLayout.setupWithViewPager(myFragmentView.viewPager)
        myFragmentView.viewPager.currentItem = pingPresenter.currentPage

        val viewPagerListener = object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (positionOffset > 0.0f)
                    scrolling = true
            }

            override fun onPageSelected(position: Int) {
                Log.e(TAG,"pageSelected = $position")
                pingPresenter.currentPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (ViewPager.SCROLL_STATE_IDLE == state) {
                    scrolling = false
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

    override fun addPingStructure(pingStructure: PingStructure){}

}