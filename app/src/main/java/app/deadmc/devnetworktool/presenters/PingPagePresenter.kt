package app.deadmc.devnetworktool.presenters

import app.deadmc.devnetworktool.interfaces.PingPageView
import app.deadmc.devnetworktool.interfaces.PingView
import app.deadmc.devnetworktool.modules.PingStructure
import com.arellomobile.mvp.InjectViewState

/**
 * Created by DEADMC on 11/22/2017.
 */

@InjectViewState
class PingPagePresenter : BasePresenter<PingPageView>() {
    fun addPingStructure(pingStructure: PingStructure, canUpdate: Boolean) {
        viewState.addPingStructure(pingStructure, canUpdate)
    }
}