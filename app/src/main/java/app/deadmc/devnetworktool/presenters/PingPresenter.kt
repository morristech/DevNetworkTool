package app.deadmc.devnetworktool.presenters

import android.os.Handler
import android.util.Log
import app.deadmc.devnetworktool.helpers.SystemHelper
import app.deadmc.devnetworktool.interfaces.views.PingView
import app.deadmc.devnetworktool.models.PingStructure
import app.deadmc.devnetworktool.shared_preferences.DevPreferences
import com.arellomobile.mvp.InjectViewState
import java.util.ArrayList

@InjectViewState
class PingPresenter : BasePresenter<PingView>() {
    var currentUrl:String = ""
    var pingStructureArrayList: ArrayList<PingStructure> = ArrayList()
    var currentPage:Int = 0
    var currentPosition:Int = 0
    private var pingThread: Thread? = null

    @Volatile private var working = false

    fun handleClick() {
        if (working) {
            working = false
            viewState.setStartButtonOn()
            viewState.hideProgress()
        } else {
            getPings()
            viewState.setStartButtonOff()
            viewState.showProgress()
        }

    }

    private fun getPings() {
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
        }
        pingThread?.start()
    }



    private fun addMessage(message: String) {
        val pingStructure = PingStructure(message)
        pingStructureArrayList.add(pingStructure)
        viewState.addPingStructure(pingStructure)
    }
}