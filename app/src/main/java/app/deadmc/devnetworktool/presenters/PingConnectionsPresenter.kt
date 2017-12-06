package app.deadmc.devnetworktool.presenters

import app.deadmc.devnetworktool.constants.DevConsts
import app.deadmc.devnetworktool.models.ConnectionHistory
import com.arellomobile.mvp.InjectViewState
import com.orm.SugarRecord

/**
 * Created by DEADMC on 11/26/2017.
 */
@InjectViewState
class PingConnectionsPresenter : ConnectionsPresenter() {

    override fun openNextFragment(mainPresenter:MainPresenter,connectionHistory: ConnectionHistory) {
        mainPresenter.runFragmentDependsOnId(DevConsts.PING_FRAGMENT,connectionHistory)
    }

    override fun fillRecyclerView() {
        val list = SugarRecord.find(ConnectionHistory::class.java, "type = ?", DevConsts.PING)
        viewState.fillRecyclerView(list)
    }
}