package app.deadmc.devnetworktool.helpers

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.DatagramPacket
import java.util.Date

/**
 * Created by Feren on 08.11.2016.
 */
object SystemHelper {

    fun getPing(url: String): String {
        return pingRequest()
    }

    external fun stringFromJNI(): String


    fun pingRequest():String {
        return stringFromJNI()
    }


}
