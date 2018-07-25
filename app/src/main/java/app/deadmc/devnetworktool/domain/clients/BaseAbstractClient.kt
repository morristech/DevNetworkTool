package app.deadmc.devnetworktool.domain.clients

import android.content.Context

import app.deadmc.devnetworktool.data.models.ConnectionHistory
import app.deadmc.devnetworktool.data.shared_preferences.Preferences
import org.kodein.di.LateInitKodein
import org.kodein.di.generic.instance

abstract class BaseAbstractClient(protected var context: Context, protected var connectionHistory: ConnectionHistory) : Runnable {

    val kodein = LateInitKodein()
    val preferences: Preferences by kodein.instance()

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
