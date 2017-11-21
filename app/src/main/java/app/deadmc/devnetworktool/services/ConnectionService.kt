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
import app.deadmc.devnetworktool.activities.MainActivity2
import app.deadmc.devnetworktool.clients.BaseAbstractClient
import app.deadmc.devnetworktool.clients.TCPClientSocket
import app.deadmc.devnetworktool.clients.UDPClientSocket
import app.deadmc.devnetworktool.constants.DevConsts
import app.deadmc.devnetworktool.modules.ConnectionHistory
import app.deadmc.devnetworktool.modules.MessageHistory
import app.deadmc.devnetworktool.presenters.MainPresenter
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


    fun setCurrentClient(currentClient: BaseAbstractClient) {
        //Log.e("service","setCurrentClient");
        this.currentClient = currentClient
        setServiceForeground(currentClient.description)
        //Log.e("service", "description " + currentClient.getDescription());
        thread = Thread(currentClient)
        thread?.start()
    }

    fun startClient() {
        setServiceForeground(currentClient?.description ?: "")
        thread = Thread(currentClient)
        thread?.start()
    }

    fun initConnection(connectionHistory: ConnectionHistory?) {
        this.connectionHistory = connectionHistory
        when (connectionHistory?.type) {
            DevConsts.TCP_CLIENT -> initTCPConnection()
            DevConsts.UDP_CLIENT -> initUDPConnection()
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
                workingConnectionPresenter?.successfulCallback()
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

    override fun onCreate() {

        setServiceForeground(application.getString(R.string.empty_service_description))

    }

    /**
     * You can run this method multiple times to update notification
     * @param description
     */
    private fun setServiceForeground(description: String) {
        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0)
        val notification: Notification
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            notification = NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.main_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.main_icon_w))
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(description)
                    .setContentIntent(pendingIntent).build()
        } else {
            notification = NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.main_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.main_icon))
                    .setContentTitle(getString(R.string.app_name))
                    .setColor(0x00ffffff)
                    .setContentText(description)
                    .setContentIntent(pendingIntent).build()
        }


        startForeground(1613, notification)
    }

    fun stopService() {
        currentClient?.close()
        thread?.interrupt()
        stopForeground(true)
        stopSelf()
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
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