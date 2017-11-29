package app.deadmc.devnetworktool.interfaces

import app.deadmc.devnetworktool.modules.PingStructure
import com.arellomobile.mvp.MvpView

/**
 * Created by DEADMC on 11/22/2017.
 */
interface PingView : MvpView {
    fun setStartButtonOn()
    fun setStartButtonOff()
    fun addPingStructure(pingStructure: PingStructure)
}