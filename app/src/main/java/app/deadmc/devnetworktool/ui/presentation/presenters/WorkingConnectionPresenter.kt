package app.deadmc.devnetworktool.ui.presentation.presenters

import android.util.Log
import app.deadmc.devnetworktool.domain.clients.BaseAbstractClient
import app.deadmc.devnetworktool.utils.currentTime
import app.deadmc.devnetworktool.ui.presentation.views.WorkingConnectionView
import app.deadmc.devnetworktool.data.models.ConnectionHistory
import app.deadmc.devnetworktool.data.models.MessageHistory
import app.deadmc.devnetworktool.data.models.ReceivedMessage
import com.arellomobile.mvp.InjectViewState
import com.orm.query.Condition
import com.orm.query.Select
import java.util.ArrayList

@InjectViewState
class WorkingConnectionPresenter : BasePresenter<WorkingConnectionView>() {
    var currentConnectionHistory: ConnectionHistory? = null
    var currentClient: BaseAbstractClient? = null

    init {
        Log.e(TAG,"created")
    }

    fun successfulCallback() {
        Log.e(TAG,"successfulCallback")
        viewState.successfulCallback()
    }

    fun addLineToAdapter(line: String, id: Long, fromServer: Boolean) {
        viewState.addLineToAdapter(ReceivedMessage(line, currentTime, id, fromServer))
    }

    fun errorCallback() {
        Log.e(TAG,"errorCallback")
        viewState.errorCallback()
    }

    fun fillReceivedMessageList() {
        val messageHistoryQuery = Select.from(MessageHistory::class.java)
                .where(Condition.prop("type").eq(currentConnectionHistory?.type),
                        Condition.prop("ip_address").eq(currentConnectionHistory?.ipAddress),
                        Condition.prop("port").eq(currentConnectionHistory?.port.toString()))
        val arrayListMessageHistory = ArrayList(messageHistoryQuery.list())
        viewState.fillReceivedMessageList(arrayListMessageHistory)
    }

    fun sendMessage(message:String) {
        Log.e("sendMessage",message)
        currentClient?.sendMessage(message)
    }
}