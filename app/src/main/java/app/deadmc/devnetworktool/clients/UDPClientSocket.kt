package app.deadmc.devnetworktool.clients

import android.content.Context
import android.util.Log

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException
import java.net.UnknownHostException

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.modules.ConnectionHistory

/**
 * Created by Feren on 23.07.2016.
 */
abstract class UDPClientSocket(context: Context, connectionHistory: ConnectionHistory) : BaseAbstractClient(context, connectionHistory) {

    @Volatile private var datagramSocket: DatagramSocket? = null

    override val description: String
        get() = context.getString(R.string.udp_connected_description)

    override fun run() {

        try {
            Log.e("datagramSocket", "started port = "+connectionHistory.port)
            datagramSocket = DatagramSocket(connectionHistory.port)
            val buffer = ByteArray(65536)
            val incomingDatagramPacket = DatagramPacket(buffer, buffer.size)
            successfulConnectCallback()
            while (true) {
                datagramSocket!!.receive(incomingDatagramPacket)
                val data = incomingDatagramPacket.data
                line = String(data, 0, incomingDatagramPacket.length)
                addLine(line!!, true)
            }
        } catch (e: Exception) {
            Log.e("UDP", Log.getStackTraceString(e))
            errorConnectCallback()
        }

    }

    override fun close() {
        if (datagramSocket != null && !datagramSocket!!.isClosed)
            datagramSocket!!.close()
    }

    override fun sendMessage(message: String) {
        val sendedMessage = message + "\n"
        val sendData = sendedMessage.toByteArray()

        Log.e("sendUdp", sendData.toString() + " " + sendData.size + " " + connectionHistory.ipAddress + " " + connectionHistory.port)
        val thread = Thread(Runnable {
            try {
                val sendPacket = DatagramPacket(sendData, sendData.size, InetAddress.getByName(connectionHistory.ipAddress), connectionHistory.port)
                val datagramSocket = DatagramSocket()
                datagramSocket.send(sendPacket)
            } catch (e: Exception) {
                Log.e("UDP",Log.getStackTraceString(e))
            }
        })
        thread.start()

        Log.e("sendMessage", "before add line")
        addLine(message, false)

    }
}