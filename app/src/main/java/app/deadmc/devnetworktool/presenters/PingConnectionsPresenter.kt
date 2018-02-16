package app.deadmc.devnetworktool.presenters


import android.util.Log
import app.deadmc.devnetworktool.constants.PING
import app.deadmc.devnetworktool.constants.PING_FRAGMENT
import app.deadmc.devnetworktool.extensions.deferredSelectDesk
import app.deadmc.devnetworktool.models.ConnectionHistory
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.experimental.launch

@InjectViewState
class PingConnectionsPresenter : ConnectionsPresenter() {

    override fun openNextFragment(mainPresenter: MainPresenter, connectionHistory: ConnectionHistory) {
        connectionHistory.setLastUsageDefault()
        mainPresenter.runFragmentDependsOnId(PING_FRAGMENT, connectionHistory)
    }

    override fun fillRecyclerView() {
        launch {
            Log.e(TAG,"coroutine")
            val list = deferredSelectDesk(ConnectionHistory::class.java, "type = ?", PING).await()
            viewState.fillRecyclerView(list)
        }
    }
}