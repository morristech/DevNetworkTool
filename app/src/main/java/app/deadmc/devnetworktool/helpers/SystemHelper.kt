package app.deadmc.devnetworktool.helpers

import android.util.Log
import app.deadmc.devnetworktool.shared_preferences.DevPreferences
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import java.io.BufferedReader
import java.io.InputStreamReader

fun executeCmd(cmd: String, sudo: Boolean): String {
    safe {
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
    }

    return ""

}

fun getPing(url: String): Deferred<String> {
    return async {
        Log.e("getPing","before delay")
        delay(DevPreferences.pingDelay.toLong())
        Log.e("getPing","after delay")
        executeCmd("ping -c 1 -w 1 " + url, false)
    }
}

