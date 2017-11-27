package app.deadmc.devnetworktool.presenters

import android.os.Handler
import android.util.Log
import android.util.SparseArray
import app.deadmc.devnetworktool.helpers.SystemHelper
import app.deadmc.devnetworktool.interfaces.PingView
import app.deadmc.devnetworktool.modules.PingStructure
import app.deadmc.devnetworktool.shared_preferences.DevPreferences
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.presenter.InjectPresenter
import java.util.ArrayList

/**
 * Created by DEADMC on 11/22/2017.
 */
@InjectViewState
class PingPresenter : BasePresenter<PingView>() {
    var currentUrl:String? = null
    var pingStructureArrayList: ArrayList<PingStructure> = ArrayList()
    var pingPagePresenterList: HashSet<PingPagePresenter> = HashSet()
    private var pingThread: Thread? = null


    @Volatile private var working = false

    fun handleClick() {
        if (working) {
            working = false
            viewState.setStartButtonOff()
        } else {
            getPings()
            viewState.setStartButtonOn()
        }

    }

    private fun getPings() {
        //Log.e("getPings", "started");
        working = true
        val handler = Handler()
        pingThread = Thread {
            while (working) {
                try {
                    Thread.sleep(DevPreferences.getPingDelay().toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                handler.post {
                    try {
                        addMessage(SystemHelper.getPing(currentUrl))
                    } catch (e: IllegalStateException) {
                        Log.e("",Log.getStackTraceString(e))
                    }
                }
            }
            Log.e("thread", "stopped")
        }
        pingThread?.start()
    }



    private fun addMessage(message: String) {
        Log.e("thread", "addMessageToPagerAdapter "+pingPagePresenterList.size)
        val pingStructure = PingStructure(message)
        pingStructureArrayList.add(pingStructure)

        pingPagePresenterList.forEach {
            Log.e("presenter","it "+it.toString())
            it.addPingStructure(pingStructure,true)
        }


        /*
        val currentItem = viewPager.getCurrentItem()
        val sparseArrayFragments = pingPagerAdapter.getRegisteredFragment()
        for (i in 0 until sparseArrayFragments.size()) {
            val index = sparseArrayFragments.keyAt(i)
            val basePingFragment = sparseArrayFragments.get(index)
            val canUpdate = index == currentItem && !scrolling
            basePingFragment.addPingStructure(pingStructure, canUpdate)
        }
        */
    }
}