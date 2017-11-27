package app.deadmc.devnetworktool.presenters

import android.util.Log
import app.deadmc.devnetworktool.constants.DevConsts
import app.deadmc.devnetworktool.modules.ConnectionHistory
import com.arellomobile.mvp.InjectViewState

/**
 * Created by DEADMC on 11/26/2017.
 */
@InjectViewState
class PingConnectionsPresenter : ConnectionsPresenter() {

    override fun openNextFragment(mainPresenter:MainPresenter,connectionHistory: ConnectionHistory) {
        Log.e("PingConnections","openNextFragment ping fragment "+DevConsts.PING_FRAGMENT)
        mainPresenter.runFragmentDependsOnId(DevConsts.PING_FRAGMENT,connectionHistory)
    }

    override fun fillRecyclerView() {
        val list = ConnectionHistory.find(ConnectionHistory::class.java, "type = ?", DevConsts.PING)
        viewState.fillRecyclerView(list)
    }
}