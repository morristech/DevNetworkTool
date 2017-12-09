package app.deadmc.devnetworktool.presenters


import app.deadmc.devnetworktool.constants.WORKING_CONNECTION_FRAGMENT
import app.deadmc.devnetworktool.interfaces.views.ConnectionsView
import app.deadmc.devnetworktool.models.ConnectionHistory
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

    open fun openNextFragment(mainPresenter:MainPresenter,connectionHistory: ConnectionHistory) {
        mainPresenter.runFragmentDependsOnId(WORKING_CONNECTION_FRAGMENT,connectionHistory)
    }

    open fun fillRecyclerView() {
    }
}