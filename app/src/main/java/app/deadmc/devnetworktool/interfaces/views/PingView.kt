package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.PingStructure
import com.arellomobile.mvp.MvpView

interface PingView : MvpView {
    fun setStartButtonOn()
    fun setStartButtonOff()
    fun showProgress()
    fun hideProgress()
    fun addPingStructure(pingStructure: PingStructure)
}