package app.deadmc.devnetworktool.interfaces

import app.deadmc.devnetworktool.modules.ConnectionHistory
import app.deadmc.devnetworktool.modules.MessageHistory
import app.deadmc.devnetworktool.modules.ReceivedMessage
import com.arellomobile.mvp.MvpView
import java.util.ArrayList

/**
 * Created by DEADMC on 11/19/2017.
 */
interface WorkingConnectionView : MvpView {
    fun successfulCallback()
    fun errorCallback()
    fun addLineToAdapter(receivedMessage: ReceivedMessage)
    fun fillReceivedMessageList(arrayListMessageHistory: ArrayList<MessageHistory>)
}