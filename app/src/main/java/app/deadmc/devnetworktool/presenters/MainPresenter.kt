package app.deadmc.devnetworktool.presenters

import app.deadmc.devnetworktool.interfaces.MainActivityView
import app.deadmc.devnetworktool.modules.ConnectionHistory
import com.arellomobile.mvp.InjectViewState
import java.io.Serializable


/**
 * Created by DEADMC on 11/11/2017.
 */
@InjectViewState
class MainPresenter : BasePresenter<MainActivityView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.runFragmentDefault()
    }

    fun runFragmentDependsOnClickedItem(item: Int) {
        viewState.runFragmentDependsOnClickedItem(item)
    }

    fun runFragmentDependsOnId(id:Int) {
        viewState.runFragmentDependsOnId(id)
    }

    fun runFragmentDependsOnId(id:Int,serializable: Serializable) {
        viewState.runFragmentDependsOnId(id,serializable)
    }

    fun showDialogExitConnection() {
        viewState.showDialogExitConnection()
    }

    fun hideDialogExitConnection() {
        viewState.hideDialogExitConnection()
    }

    fun doBindService(connectionHistory: ConnectionHistory?) {
        viewState.doBindService(connectionHistory)
    }

    fun startConnectionClient(connectionHistory: ConnectionHistory) {

    }

    fun setCustomTitle(stringId: Int) {
        viewState.setCustomTitle(stringId)
    }

    fun setCustomTitle(title: String) {
        viewState.setCustomTitle(title)
    }

    fun stopService() {
        viewState.stopService()
    }

}