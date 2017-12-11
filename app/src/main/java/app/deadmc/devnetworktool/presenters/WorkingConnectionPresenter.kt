package app.deadmc.devnetworktool.presenters

import android.util.Log
import app.deadmc.devnetworktool.clients.BaseAbstractClient
import app.deadmc.devnetworktool.helpers.currentTime
import app.deadmc.devnetworktool.interfaces.views.WorkingConnectionView
import app.deadmc.devnetworktool.models.ConnectionHistory
import app.deadmc.devnetworktool.models.MessageHistory
import app.deadmc.devnetworktool.models.ReceivedMessage
import com.arellomobile.mvp.InjectViewState
import com.orm.query.Condition
import com.orm.query.Select
import java.util.ArrayList

@InjectViewState
class WorkingConnectionPresenter : BasePresenter<WorkingConnectionView>() {
    var currentConnectionHistory: ConnectionHistory? = null
    var currentClient: BaseAbstractClient? = null

    fun successfulCallback() {
        viewState.successfulCallback()
    }

    fun addLineToAdapter(line: String, id: Long, fromServer: Boolean) {
        viewState.addLineToAdapter(ReceivedMessage(line, currentTime, id, fromServer))
    }

    fun errorCallback() {
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