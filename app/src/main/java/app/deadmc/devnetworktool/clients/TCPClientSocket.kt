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

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.helpers.isValidIp
import app.deadmc.devnetworktool.models.ConnectionHistory
import okio.Okio
import java.io.BufferedWriter
import java.io.OutputStreamWriter

abstract class TCPClientSocket(context: Context, connectionHistory: ConnectionHistory) : BaseAbstractClient(context, connectionHistory) {

    @Volatile private var socket: Socket? = null

    override val description: String
        get() = context.getString(R.string.tcp_connected_description)

    override fun run() {
        try {
            socket = Socket()

            if (isValidIp(connectionHistory.ipAddress)) {
                socket?.connect(InetSocketAddress(connectionHistory.ipAddress, connectionHistory.port), 2000)
            } else {
                if (!connectionHistory.ipAddress.contains("http://") && !connectionHistory.ipAddress.contains("https://"))
                    connectionHistory.ipAddress = "http://" + connectionHistory.ipAddress
                val address = InetAddress.getByName(URL(connectionHistory.ipAddress).host)
                socket?.connect(InetSocketAddress(address, connectionHistory.port), 2000)
            }

            val bufferedSource = Okio.buffer(Okio.source(socket!!))

            successfulConnectCallback()
            while (!bufferedSource.exhausted()) {
                line = bufferedSource.readUtf8(bufferedSource.buffer().size())
                addLine(line!!, true)
            }


        } catch (e: Exception) {
            Log.e("TCP","exception "+Log.getStackTraceString(e))
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
        val messageLn =message+"\n"
        try {
            //val dataOutputStream = DataOutputStream(socket!!.getOutputStream())
            val outputStreamWriter = OutputStreamWriter(socket!!.getOutputStream(), "UTF-8")
            outputStreamWriter.write(messageLn)
            Log.e(TAG,"socket "+socket!!.inetAddress.toString())
            outputStreamWriter.flush()
            Log.e(TAG,"flush")
            //outputStreamWriter.flush()
            //outputStreamWriter.append(message).append("\n").flush()
            addLine(message, false)
            Log.e(TAG,"finally")
        } catch (e: Exception) {
            Log.e(TAG,"exception")
            Log.e(TAG,Log.getStackTraceString(e))
        }

    }
}
