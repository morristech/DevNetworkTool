package app.deadmc.devnetworktool.fragments.rest

import android.os.Bundle
import android.os.StrictMode
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.HashMap

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.adapters.RestPagerAdapter
import app.deadmc.devnetworktool.constants.DevConsts
import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.fragments.ObserverFragment
import app.deadmc.devnetworktool.fragments.ParentFragment
import app.deadmc.devnetworktool.modules.ResponseDev
import app.deadmc.devnetworktool.modules.RestRequestHistory
import app.deadmc.devnetworktool.observables.OkHttpObservable
import app.deadmc.devnetworktool.presenters.RestPresenter
import app.deadmc.devnetworktool.singletons.SharedData
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_rest.view.*
import okhttp3.Response

class RestFragment : BaseFragment() {
    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var restPresenter:RestPresenter
    private lateinit var restPagerAdapter: RestPagerAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater!!.inflate(R.layout.fragment_rest, container, false)
        restoreState(savedInstanceState)
        initElements()
        return myFragmentView
    }

    private fun restoreState(bundle: Bundle?) {
        if (bundle == null)
            return
        if (!bundle.containsKey("currentUrl"))
            return
        restPresenter.currentUrl = bundle.getString("currentUrl")

    }

    fun initElements() {
        //createObserver(Response::class.java)
        initViewPager()
    }

    fun initViewPager() {
        restPagerAdapter = RestPagerAdapter(fragmentManager, activity, this)
        myFragmentView.viewPager.adapter = restPagerAdapter
       myFragmentView.viewPager.offscreenPageLimit = 5
        myFragmentView.tabLayout.setupWithViewPager(myFragmentView.viewPager)
       myFragmentView.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    val parentFragmentSparseArray = restPagerAdapter.registeredFragment
                    for (i in 0 until parentFragmentSparseArray.size()) {
                        val key = parentFragmentSparseArray.keyAt(i)
                        val parentFragment = parentFragmentSparseArray.get(key)
                        (parentFragment as? RestHistoryFragment)?.initElements()
                    }
                }

            }
        })
    }



    private fun notifyAllNestedFragments(responseDev: ResponseDev) {
        SharedData.getInstance().responseDev = responseDev
        val parentFragmentSparseArray = restPagerAdapter!!.registeredFragment
        for (i in 0 until parentFragmentSparseArray.size()) {
            val key = parentFragmentSparseArray.keyAt(i)
            val parentFragment = parentFragmentSparseArray.get(key)
            if (parentFragment is ResponseRestFragment) {
                try {
                    parentFragment.setResponse(responseDev)
                } catch (e: Exception) {
                }

            }
        }
    }

    fun loadRestHistory(restRequestHistory: RestRequestHistory) {
        val parentFragmentSparseArray = restPagerAdapter!!.registeredFragment
        for (i in 0 until parentFragmentSparseArray.size()) {
            val key = parentFragmentSparseArray.keyAt(i)
            val parentFragment = parentFragmentSparseArray.get(key)
            /*
                if (parentFragment instanceof  RequestRestFragment) {
                    RequestRestFragment requestRestFragment = (RequestRestFragment) parentFragment;
                    requestRestFragment.setRequestHistory(restRequestHistory);
                }
                */
        }
       myFragmentView.viewPager.setCurrentItem(0, true)
    }

    fun setRestFragment() {
        val parentFragmentSparseArray = restPagerAdapter!!.registeredFragment
        for (i in 0 until parentFragmentSparseArray.size()) {
            val key = parentFragmentSparseArray.keyAt(i)
            val parentFragment = parentFragmentSparseArray.get(key)
            /*
            if (parentFragment instanceof  RequestRestFragment) {
                RequestRestFragment requestRestFragment = (RequestRestFragment) parentFragment;
                requestRestFragment.setRestFragment(RestFragment.this);
            }

            if (parentFragment instanceof  RestHistoryFragment) {
                RestHistoryFragment requestRestFragment = (RestHistoryFragment) parentFragment;
                requestRestFragment.setRestFragment(RestFragment.this);
            }
            */
        }
    }

    override fun handleResult(value: Any) {

        val responseDev = value as ResponseDev
        unsubscribe()
        notifyAllNestedFragments(responseDev)

    }

}
