package app.deadmc.devnetworktool.presenters


import app.deadmc.devnetworktool.constants.TCP_CLIENT
import app.deadmc.devnetworktool.models.ConnectionHistory
import com.arellomobile.mvp.InjectViewState
import com.orm.SugarRecord

@InjectViewState
class TcpConnectionsPresenter : ConnectionsPresenter() {

    override fun fillRecyclerView() {
        val list = SugarRecord.find(ConnectionHistory::class.java, "type = ?", TCP_CLIENT)
        viewState.fillRecyclerView(list)
    }
}