package app.deadmc.devnetworktool.clients

import android.content.Context
import android.util.Log

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.models.ConnectionHistory
import app.deadmc.devnetworktool.shared_preferences.DevPreferences
import java.nio.charset.Charset

abstract class UDPClientSocket(context: Context, connectionHistory: ConnectionHistory) : BaseAbstractClient(context, connectionHistory) {

    @Volatile private var datagramSocket: DatagramSocket? = null

    override val description: String
        get() = context.getString(R.string.udp_connected_description)

    override fun run() {

        try {
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
        val sendData = sendedMessage.toByteArray(Charset.forName(DevPreferences.tcpUdpEncoding))
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
        addLine(message, false)

    }
}
