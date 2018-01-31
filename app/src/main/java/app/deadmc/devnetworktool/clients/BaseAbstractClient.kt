package app.deadmc.devnetworktool.clients

import android.content.Context

import app.deadmc.devnetworktool.models.ConnectionHistory

abstract class BaseAbstractClient(protected var context: Context, protected var connectionHistory: ConnectionHistory) : Runnable {
    protected var line: String? = null
    protected val TAG = javaClass.simpleName
    abstract val description: String
    abstract fun addLine(line: String, fromServer: Boolean)
    abstract fun sendMessage(message: String)
    abstract fun successfulConnectCallback()
    abstract fun errorConnectCallback()
    abstract fun close()


    override fun run() {

    }

}
