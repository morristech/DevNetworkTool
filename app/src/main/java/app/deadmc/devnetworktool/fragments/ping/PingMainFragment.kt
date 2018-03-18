package app.deadmc.devnetworktool.fragments.ping

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.adapters.PingPagerAdapter
import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.interfaces.views.PingMainView
import app.deadmc.devnetworktool.models.ConnectionHistory
import app.deadmc.devnetworktool.presenters.BasePresenter
import app.deadmc.devnetworktool.presenters.PingMainPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_ping.view.*
import kotlinx.android.synthetic.main.horizontal_progress_bar.view.*
import java.io.Serializable

class PingMainFragment : BaseFragment(), PingMainView {

    @InjectPresenter
    lateinit var pingPresenter:PingMainPresenter
    lateinit var pingPagerAdapter: PingPagerAdapter
    private var scrolling = false

    companion object {
        fun getInstance(serializable: Serializable): PingMainFragment {
            val fragment = PingMainFragment()
            val args = Bundle()
            args.putSerializable("connection_history", serializable)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentConnectionHistory = this.arguments?.getSerializable("connection_history") as ConnectionHistory
        pingPresenter.currentUrl = currentConnectionHistory.ipAddress
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater.inflate(R.layout.fragment_ping, container, false)
        initElements()
        return myFragmentView
    }

    override fun getPresenter(): BasePresenter<*> {
        return pingPresenter
    }

    fun initElements() {
        initViewPager()
        initButton()
        hideProgress()
    }

    override fun setStartButtonOn() {
        myFragmentView.startButton.text = getString(R.string.start)
    }

    override fun setStartButtonOff() {
        myFragmentView.startButton.text = getString(R.string.stop)
    }

    override fun showProgress() {
        myFragmentView.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        myFragmentView.progressBar.visibility = View.INVISIBLE
    }


    private fun initViewPager() {
        pingPagerAdapter = PingPagerAdapter(fragmentManager!!, activity!!, pingPresenter.currentUrl)
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
                pingPresenter.currentPage = position
                myFragmentView.viewPager.swipeEnabled = position != 1
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


}