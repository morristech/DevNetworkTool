package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.MessageHistory
import app.deadmc.devnetworktool.models.ReceivedMessage
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