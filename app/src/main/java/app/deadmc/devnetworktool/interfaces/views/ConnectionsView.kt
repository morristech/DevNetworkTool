package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.ConnectionHistory
import com.arellomobile.mvp.MvpView

interface ConnectionsView : MvpView {
    fun showDialogForCreate()
    fun showDialogForEdit(connectionHistory: ConnectionHistory, position: Int)
    fun showEmpty()
    fun showView()
    fun hideDialog()
    fun fillRecyclerView(list:List<ConnectionHistory>)
}