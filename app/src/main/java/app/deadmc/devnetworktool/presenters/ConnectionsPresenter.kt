package app.deadmc.devnetworktool.presenters

import app.deadmc.devnetworktool.constants.DevConsts
import app.deadmc.devnetworktool.interfaces.ConnectionsView
import app.deadmc.devnetworktool.modules.ConnectionHistory
import com.arellomobile.mvp.InjectViewState

@InjectViewState
open class ConnectionsPresenter : BasePresenter<ConnectionsView>() {
    var currentConnectionHistory = ConnectionHistory()

    fun saveConnectionHistory(connectionHistory: ConnectionHistory) {
        connectionHistory.save()
    }

    fun deleteConnectionHistory(connectionHistory: ConnectionHistory) {
        connectionHistory.delete()
    }

    fun showDialogForCreate() {
        viewState.showDialogForCreate()
    }

    fun showDialogForEdit(connectionHistory: ConnectionHistory, position:Int) {
        viewState.showDialogForEdit(connectionHistory, position)
    }

    fun hideDialog() {
        viewState.hideDialog()
    }

    fun openWorkingConnectionFragment(mainPresenter:MainPresenter,connectionHistory: ConnectionHistory) {
        mainPresenter.runFragmentDependsOnId(DevConsts.WORKING_CONNECTION_FRAGMENT,connectionHistory)
    }

    open fun fillRecyclerView() {
    }
}