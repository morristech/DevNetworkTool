package app.deadmc.devnetworktool.clients

import android.content.Context

import app.deadmc.devnetworktool.modules.ConnectionHistory

/**
 * Created by Feren on 23.07.2016.
 */
abstract class BaseAbstractClient(protected var context: Context, protected var connectionHistory: ConnectionHistory) : Runnable {
    protected var line: String? = null
    abstract val description: String
    abstract fun addLine(line: String, fromServer: Boolean)
    abstract fun sendMessage(message: String)
    abstract fun successfulConnectCallback()
    abstract fun errorConnectCallback()
    abstract fun close()


    override fun run() {

    }

}
