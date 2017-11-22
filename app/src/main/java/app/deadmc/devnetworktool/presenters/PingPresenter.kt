package app.deadmc.devnetworktool.presenters

import android.os.Handler
import android.util.Log
import android.util.SparseArray
import app.deadmc.devnetworktool.helpers.SystemHelper
import app.deadmc.devnetworktool.interfaces.PingView
import app.deadmc.devnetworktool.modules.PingStructure
import app.deadmc.devnetworktool.shared_preferences.DevPreferences
import java.util.ArrayList

/**
 * Created by DEADMC on 11/22/2017.
 */
class PingPresenter : BasePresenter<PingView>() {
    var currentUrl:String? = null
    private var pingStructureArrayList: ArrayList<PingStructure> = ArrayList()
    private var pingThread: Thread? = null
    private var pingPagePresenterList: ArrayList<PingPagePresenter> = ArrayList()

    @Volatile private var working = false

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
                        addMessageToPagerAdapter(SystemHelper.getPing(currentUrl))
                    } catch (e: IllegalStateException) {
                        Log.e("",Log.getStackTraceString(e))
                    }
                }
            }
            Log.e("thread", "stopped")
        }
        pingThread?.start()
    }

    private fun addMessageToPagerAdapter(message: String) {
        Log.e("thread", "addMessageToPagerAdapter")
        val pingStructure = PingStructure(message)
        pingStructureArrayList.add(pingStructure)

        val currentItem = viewPager.getCurrentItem()

        val sparseArrayFragments = pingPagerAdapter.getRegisteredFragment()

        for (i in 0 until sparseArrayFragments.size()) {
            val index = sparseArrayFragments.keyAt(i)
            val basePingFragment = sparseArrayFragments.get(index)
            val canUpdate = index == currentItem && !scrolling
            basePingFragment.addPingStructure(pingStructure, canUpdate)
        }
    }
}