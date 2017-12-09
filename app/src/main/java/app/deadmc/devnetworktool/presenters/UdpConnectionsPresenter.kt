package app.deadmc.devnetworktool.presenters


import app.deadmc.devnetworktool.constants.UDP_CLIENT
import app.deadmc.devnetworktool.models.ConnectionHistory
import com.arellomobile.mvp.InjectViewState
import com.orm.SugarRecord

@InjectViewState
class UdpConnectionsPresenter : ConnectionsPresenter() {

    override fun fillRecyclerView() {
        val list = SugarRecord.find(ConnectionHistory::class.java, "type = ?", UDP_CLIENT)
        viewState.fillRecyclerView(list)
    }
}