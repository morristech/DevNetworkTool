package app.deadmc.devnetworktool.presenters

import app.deadmc.devnetworktool.constants.DevConsts
import app.deadmc.devnetworktool.modules.ConnectionHistory
import com.arellomobile.mvp.InjectViewState

/**
 * Created by DEADMC on 11/19/2017.
 */
@InjectViewState
class UdpConnectionsPresenter : ConnectionsPresenter() {

    override fun fillRecyclerView() {
        val list = ConnectionHistory.find(ConnectionHistory::class.java, "type = ?", DevConsts.UDP_CLIENT)
        viewState.fillRecyclerView(list)
    }
}