package app.deadmc.devnetworktool.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v7.app.NotificationCompat
import android.util.Log

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.activities.MainActivity
import app.deadmc.devnetworktool.clients.BaseAbstractClient
import app.deadmc.devnetworktool.clients.TCPClientSocket
import app.deadmc.devnetworktool.clients.UDPClientSocket
import app.deadmc.devnetworktool.constants.TCP_CLIENT
import app.deadmc.devnetworktool.constants.UDP_CLIENT
import app.deadmc.devnetworktool.helpers.getNotification

import app.deadmc.devnetworktool.models.ConnectionHistory
import app.deadmc.devnetworktool.models.MessageHistory
import app.deadmc.devnetworktool.presenters.WorkingConnectionPresenter

class ConnectionService : Service() {

    var workingConnectionPresenter: WorkingConnectionPresenter? = null
    var connectionHistory: ConnectionHistory? = null


    private val binder = LocalBinder()
    private var currentClient: BaseAbstractClient? = null
    private var thread: Thread? = null


    var isRunning = false
        private set


    inner class LocalBinder : Binder() {
        val service: ConnectionService
            get() = this@ConnectionService
    }

    fun startClient() {
        setServiceForeground(currentClient?.description ?: "")
        thread = Thread(currentClient)
        thread?.start()
    }

    fun initConnection(connectionHistory: ConnectionHistory?) {
        this.connectionHistory = connectionHistory
        when (connectionHistory?.type) {
            TCP_CLIENT -> initTCPConnection()
            UDP_CLIENT -> initUDPConnection()
        }
        workingConnectionPresenter?.currentClient = currentClient
        startClient()
    }

    private fun initTCPConnection() {
        currentClient = object : TCPClientSocket(applicationContext, connectionHistory!!) {
            override fun addLine(line: String, fromServer: Boolean) {
                val id = saveMessageToDatabase(line, fromServer)
                workingConnectionPresenter?.addLineToAdapter(line, id, fromServer)
            }

            override fun successfulConnectCallback() {
                isRunning = true
            }

            override fun errorConnectCallback() {
                workingConnectionPresenter?.errorCallback()
                currentClient = null
                isRunning = false
                stopService()
            }
        }
    }

    private fun initUDPConnection() {
        //Log.e("addLineUdp", "initUDPConnection");
        currentClient = object : UDPClientSocket(applicationContext, connectionHistory!!) {
            override fun addLine(line: String, fromServer: Boolean) {
                val id = saveMessageToDatabase(line, fromServer)
                workingConnectionPresenter?.addLineToAdapter(line, id, fromServer)
            }

            override fun successfulConnectCallback() {
                isRunning = true
                workingConnectionPresenter?.successfulCallback()
            }

            override fun errorConnectCallback() {
                isRunning = false
                currentClient = null
                stopService()
                workingConnectionPresenter?.errorCallback()
            }
        }
    }

    private fun saveMessageToDatabase(message: String, fromServer: Boolean): Long {
        val messageHistory = MessageHistory()
        messageHistory.timeAdded = System.currentTimeMillis()
        messageHistory.type = connectionHistory!!.type
        messageHistory.ipAddress = connectionHistory!!.ipAddress
        messageHistory.port = connectionHistory!!.port
        messageHistory.isFromServer = fromServer
        messageHistory.message = message
        return messageHistory.save()
    }


    fun getCurrentClient(): BaseAbstractClient? {
        return currentClient
    }

    /**
     * You can run this method multiple times to update notification
     * @param description
     */
    private fun setServiceForeground(description: String) {
        startForeground(1613, getNotification(this,description))
    }

    fun stopService() {
        currentClient?.close()
        thread?.interrupt()
        stopForeground(true)
        stopSelf()
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        setServiceForeground(application.getString(R.string.empty_service_description))
        return Service.START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("service", "onDestroy")
    }
}