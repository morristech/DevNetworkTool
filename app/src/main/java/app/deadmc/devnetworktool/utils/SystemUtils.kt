package app.deadmc.devnetworktool.utils

import android.util.Log
import app.deadmc.devnetworktool.data.shared_preferences.Preferences
import app.deadmc.devnetworktool.data.shared_preferences.PreferencesImpl
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import org.kodein.di.LateInitKodein
import org.kodein.di.generic.instance
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
        val kodein = LateInitKodein()
        val preferences: Preferences by kodein.instance()
        delay(preferences.pingDelay.toLong())
        Log.e("getPing","after delay")
        executeCmd("ping -c 1 -w 1 " + url, false)
    }
}

