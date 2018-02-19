package app.deadmc.devnetworktool.application

import android.app.Application

import com.crashlytics.android.Crashlytics
import com.orm.SugarContext

import app.deadmc.devnetworktool.models.ConnectionHistory
import app.deadmc.devnetworktool.shared_preferences.DevPreferences
import io.fabric.sdk.android.Fabric

import app.deadmc.devnetworktool.constants.PING
import app.deadmc.devnetworktool.constants.TCP_CLIENT
import app.deadmc.devnetworktool.constants.UDP_CLIENT
import com.orm.SchemaGenerator
import com.orm.SugarDb

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        SugarContext.init(applicationContext)
        DevPreferences.init(applicationContext)

        if (DevPreferences.firstLaunch) {
            createDefaultData()
            DevPreferences.setFirstLaunch(false)
        }
    }

    private fun createDefaultData() {
        SchemaGenerator(this).createDatabase(SugarDb(this).db)
        ConnectionHistory("test tcp connection", "http://google.com", 80, TCP_CLIENT).save()
        ConnectionHistory("test udp connection", "127.0.0.1", 1055, UDP_CLIENT).save()
        ConnectionHistory("test ping", "http://www.speedtest.net/", 80, PING).save()
    }
}
