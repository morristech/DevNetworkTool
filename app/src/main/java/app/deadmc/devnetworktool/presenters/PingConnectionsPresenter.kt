package app.deadmc.devnetworktool.presenters


import android.util.Log
import app.deadmc.devnetworktool.constants.PING
import app.deadmc.devnetworktool.constants.PING_FRAGMENT
import app.deadmc.devnetworktool.extensions.deferredSelectDesc
import app.deadmc.devnetworktool.helpers.safe
import app.deadmc.devnetworktool.models.ConnectionHistory
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.asReference

@InjectViewState
class PingConnectionsPresenter : ConnectionsPresenter() {

    override fun openNextFragment(mainPresenter: MainPresenter, connectionHistory: ConnectionHistory) {
        //connectionHistory.setLastUsageDefault()
        mainPresenter.runFragmentDependsOnId(PING_FRAGMENT, connectionHistory)
    }

    override fun fillRecyclerView() {
        val ref = viewState.asReference()
        async(UI) {
            Log.e(TAG,"coroutine")
            safe {
                val list = deferredSelectDesc(ConnectionHistory::class.java, "type = ?", PING).await()
                ref().fillRecyclerView(list)
            }
        }
    }
}