package app.deadmc.devnetworktool.presenters


import app.deadmc.devnetworktool.constants.PING
import app.deadmc.devnetworktool.constants.PING_FRAGMENT
import app.deadmc.devnetworktool.models.ConnectionHistory
import com.arellomobile.mvp.InjectViewState
import com.orm.SugarRecord

@InjectViewState
class PingConnectionsPresenter : ConnectionsPresenter() {

    override fun openNextFragment(mainPresenter:MainPresenter,connectionHistory: ConnectionHistory) {
        mainPresenter.runFragmentDependsOnId(PING_FRAGMENT,connectionHistory)
    }

    override fun fillRecyclerView() {
        val list = SugarRecord.find(ConnectionHistory::class.java, "type = ?", PING)
        viewState.fillRecyclerView(list)
    }
}