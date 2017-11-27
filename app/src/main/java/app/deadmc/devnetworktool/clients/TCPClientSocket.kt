package app.deadmc.devnetworktool.clients

import android.content.Context
import android.os.StrictMode
import android.util.Log

import java.io.DataOutputStream
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.net.URL
import java.net.UnknownHostException

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.helpers.CheckHelper
import app.deadmc.devnetworktool.modules.ConnectionHistory
import okio.BufferedSource
import okio.Okio

/**
 * Created by Feren on 09.06.2016.
 */
abstract class TCPClientSocket(context: Context, connectionHistory: ConnectionHistory) : BaseAbstractClient(context, connectionHistory) {

    @Volatile private var socket: Socket? = null

    override val description: String
        get() = context.getString(R.string.tcp_connected_description)

    override fun run() {
        try {
            socket = Socket()
            if (connectionHistory.ipAddress == null) {
                errorConnectCallback()
                return
            }

            if (CheckHelper.isValidIp(connectionHistory.ipAddress)) {
                socket?.connect(InetSocketAddress(connectionHistory.ipAddress, connectionHistory.port), 2000)
            } else {
                if (!connectionHistory.ipAddress.contains("http://") && !connectionHistory.ipAddress.contains("https://"))
                    connectionHistory.ipAddress = "http://" + connectionHistory.ipAddress
                val address = InetAddress.getByName(URL(connectionHistory.ipAddress).host)
                Log.e("customAddress", " = " + address.hostAddress)
                socket?.connect(InetSocketAddress(address, connectionHistory.port), 2000)
            }

            val bufferedSource = Okio.buffer(Okio.source(socket!!))

            successfulConnectCallback()
            Log.e("TCP", "looks like everything is ok")
            while (!bufferedSource.exhausted()) {
                line = bufferedSource.readUtf8(bufferedSource.buffer().size())
                addLine(line!!, true)
            }


        } catch (e: Exception) {
            Log.e("TCP","exceotion "+Log.getStackTraceString(e))
            errorConnectCallback()
        }

    }

    override fun close() {
        try {
            Log.e("close", "tcp socket closed")
            socket?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun sendMessage(message: String) {
        Log.e("TCPClient",message)
        try {
            val dataOutputStream = DataOutputStream(socket?.getOutputStream())
            dataOutputStream.writeChars(message + "\n")
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            addLine(message, false)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
        } catch (e: Exception) {
        }

    }
}