package app.deadmc.devnetworktool.presenters

import app.deadmc.devnetworktool.constants.DevConsts
import app.deadmc.devnetworktool.modules.ConnectionHistory
import com.arellomobile.mvp.InjectViewState

/**
 * Created by DEADMC on 11/15/2017.
 */
@InjectViewState
class TcpConnectionsPresenter : ConnectionsPresenter() {

    override fun fillRecyclerView() {
        val list = ConnectionHistory.find(ConnectionHistory::class.java, "type = ?", DevConsts.TCP_CLIENT)
        viewState.fillRecyclerView(list)
    }
}