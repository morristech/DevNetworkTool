package app.deadmc.devnetworktool.ui.presentation.presenters


import app.deadmc.devnetworktool.constants.WORKING_CONNECTION_FRAGMENT
import app.deadmc.devnetworktool.extensions.asyncDelete
import app.deadmc.devnetworktool.extensions.asyncSave
import app.deadmc.devnetworktool.ui.presentation.views.ConnectionsView
import app.deadmc.devnetworktool.data.models.ConnectionHistory
import com.arellomobile.mvp.InjectViewState

@InjectViewState
open class ConnectionsPresenter : BasePresenter<ConnectionsView>() {
    var currentConnectionHistory = ConnectionHistory()

    fun saveConnectionHistory(connectionHistory: ConnectionHistory) {
        connectionHistory.asyncSave()
    }

    fun deleteConnectionHistory(connectionHistory: ConnectionHistory) {
        connectionHistory.asyncDelete()
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

    open fun openNextFragment(mainPresenter: MainPresenter, connectionHistory: ConnectionHistory) {
        connectionHistory.setLastUsageDefault()
        connectionHistory.asyncSave()
        mainPresenter.runFragmentDependsOnId(WORKING_CONNECTION_FRAGMENT,connectionHistory)
    }

    open fun fillRecyclerView() {
    }
}