package app.deadmc.devnetworktool.presenters

import android.util.Log
import app.deadmc.devnetworktool.clients.BaseAbstractClient
import app.deadmc.devnetworktool.helpers.DateTimeHelper
import app.deadmc.devnetworktool.interfaces.WorkingConnectionView
import app.deadmc.devnetworktool.modules.ConnectionHistory
import app.deadmc.devnetworktool.modules.MessageHistory
import app.deadmc.devnetworktool.modules.ReceivedMessage
import com.arellomobile.mvp.InjectViewState
import com.orm.query.Condition
import com.orm.query.Select
import java.util.ArrayList

/**
 * Created by DEADMC on 11/19/2017.
 */
@InjectViewState
class WorkingConnectionPresenter : BasePresenter<WorkingConnectionView>() {
    var currentConnectionHistory: ConnectionHistory? = null
    var currentClient: BaseAbstractClient? = null

    fun successfulCallback() {
        viewState.successfulCallback()
    }

    fun addLineToAdapter(line: String, id: Long, fromServer: Boolean) {
        viewState.addLineToAdapter(ReceivedMessage(line, DateTimeHelper.getCurrentTime(), id, fromServer))
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