package app.deadmc.devnetworktool.helpers

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

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
            if (s != null) res += s
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

