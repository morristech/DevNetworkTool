package app.deadmc.devnetworktool.helpers

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by Feren on 08.11.2016.
 */
object SystemHelper {

    fun executeCmd(cmd: String, sudo: Boolean): String {
        try {

            val p: Process
            if (!sudo)
                p = Runtime.getRuntime().exec(cmd)
            else {
                p = Runtime.getRuntime().exec(arrayOf("su", "-c", cmd))
            }
            val stdInput = BufferedReader(InputStreamReader(p.inputStream))

            var s: String? = ""
            var res = ""
            while (s != null) {
                s = stdInput.readLine()
                if (s!=null) res += s
            }
            p.destroy()
            return res
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""

    }

    fun getPing(url: String): String {
        return executeCmd("ping -c 1 -w 1 " + url, false)
    }

    /*
    fun getPing(url: String): String {
        return pingRequest()
    }
    */

    external fun stringFromJNI(): String


    fun pingRequest(): String {
        return stringFromJNI()
    }


    // Used to load the 'native-lib' library on application startup.
    init {
        System.loadLibrary("native-lib")
    }


}
