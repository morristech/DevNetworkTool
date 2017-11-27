package app.deadmc.devnetworktool.presenters

import app.deadmc.devnetworktool.constants.DevConsts
import app.deadmc.devnetworktool.modules.ConnectionHistory
import com.arellomobile.mvp.InjectViewState

/**
 * Created by DEADMC on 11/26/2017.
 */
@InjectViewState
class RestConnectionsFragment : ConnectionsPresenter() {

    override fun openNextFragment(mainPresenter:MainPresenter,connectionHistory: ConnectionHistory) {
        mainPresenter.runFragmentDependsOnId(DevConsts.PING_FRAGMENT,connectionHistory)
    }

    override fun fillRecyclerView() {
        val list = ConnectionHistory.find(ConnectionHistory::class.java, "type = ?", DevConsts.PING)
        viewState.fillRecyclerView(list)
    }
}