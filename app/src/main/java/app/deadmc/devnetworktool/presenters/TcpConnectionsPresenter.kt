package app.deadmc.devnetworktool.presenters


import app.deadmc.devnetworktool.constants.TCP_CLIENT
import app.deadmc.devnetworktool.extensions.deferredSelectDesc
import app.deadmc.devnetworktool.models.ConnectionHistory
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.experimental.launch

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
        launch {
            val list = deferredSelectDesc(ConnectionHistory::class.java, "type = ?", TCP_CLIENT).await()
            viewState.fillRecyclerView(list)
        }
    }
}