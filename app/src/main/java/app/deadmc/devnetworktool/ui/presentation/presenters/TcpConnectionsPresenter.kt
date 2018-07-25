package app.deadmc.devnetworktool.ui.presentation.presenters


import app.deadmc.devnetworktool.constants.TCP_CLIENT
import app.deadmc.devnetworktool.extensions.deferredSelectDesc
import app.deadmc.devnetworktool.data.models.ConnectionHistory
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.asReference

@InjectViewState
class TcpConnectionsPresenter : ConnectionsPresenter() {

    /*
    override fun fillRecyclerView() {
        val list = SugarRecord.find(ConnectionHistory::class.java, "type = ?", TCP_CLIENT)
        viewState.fillRecyclerView(list)
    }
    */

    override fun fillRecyclerView() {
        //val list = SugarRecord.find(ConnectionHistory::class.java, "type = ?", UDP_CLIENT)
        val ref = viewState.asReference()
        async(UI) {
            val list = deferredSelectDesc(ConnectionHistory::class.java, "type = ?", TCP_CLIENT).await()
            ref().fillRecyclerView(list)
        }
    }
}