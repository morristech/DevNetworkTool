package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.PingStructure
import com.arellomobile.mvp.MvpView

interface PingView : MvpView {

    fun addPingStructure(pingStructure: PingStructure)
}