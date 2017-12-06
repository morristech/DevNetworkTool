package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.ConnectionHistory
import com.arellomobile.mvp.MvpView

/**
 * Created by DEADMC on 11/19/2017.
 */
interface ConnectionsView : MvpView {
    fun showDialogForCreate()
    fun showDialogForEdit(connectionHistory: ConnectionHistory, position: Int)
    fun hideDialog()
    fun fillRecyclerView(list:List<ConnectionHistory>)
}