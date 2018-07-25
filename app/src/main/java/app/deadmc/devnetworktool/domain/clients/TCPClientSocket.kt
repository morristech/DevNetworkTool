package app.deadmc.devnetworktool.domain.clients

import android.content.Context
import android.util.Log
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.utils.safe
import app.deadmc.devnetworktool.data.models.ConnectionHistory
import app.deadmc.devnetworktool.data.shared_preferences.Preferences
import app.deadmc.devnetworktool.extensions.isValidIp
import okio.Okio
import org.kodein.di.LateInitKodein
import org.kodein.di.generic.instance
import java.io.OutputStreamWriter
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.net.URL

abstract class TCPClientSocket(context: Context, connectionHistory: ConnectionHistory) : BaseAbstractClient(context, connectionHistory) {

    @Volatile private var socket: Socket? = null



    override val description: String
        get() = context.getString(R.string.tcp_connected_description)

    override fun run() {
        try {
            socket = Socket()

            if (connectionHistory.ipAddress.isValidIp()) {
                socket?.connect(InetSocketAddress(connectionHistory.ipAddress, connectionHistory.port), preferences.tcpTimeoutAmount)
            } else {
                if (!connectionHistory.ipAddress.contains("http://") && !connectionHistory.ipAddress.contains("https://"))
                    connectionHistory.ipAddress = "http://" + connectionHistory.ipAddress
                val address = InetAddress.getByName(URL(connectionHistory.ipAddress).host)
                socket?.connect(InetSocketAddress(address, connectionHistory.port), preferences.tcpTimeoutAmount)
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
        safe {
            socket?.close()
        }
    }

    override fun sendMessage(message: String) {
        val messageLn = message+"\n"
        Thread {
            safe {
                val outputStreamWriter = OutputStreamWriter(socket!!.getOutputStream(), preferences.tcpUdpEncoding)
                outputStreamWriter.write(messageLn)
                outputStreamWriter.flush()
                addLine(message, false)
            }
        }.start()

    }
}
