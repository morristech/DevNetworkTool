package app.deadmc.devnetworktool.application

import android.app.Application

import com.crashlytics.android.Crashlytics
import com.orm.SugarContext

import app.deadmc.devnetworktool.data.models.ConnectionHistory
import app.deadmc.devnetworktool.data.shared_preferences.PreferencesImpl
import io.fabric.sdk.android.Fabric

import app.deadmc.devnetworktool.constants.PING
import app.deadmc.devnetworktool.constants.TCP_CLIENT
import app.deadmc.devnetworktool.constants.UDP_CLIENT
import app.deadmc.devnetworktool.data.shared_preferences.Preferences
import com.orm.SchemaGenerator
import com.orm.SugarDb
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance

class MainApplication : Application(), KodeinAware {
    override val kodein = Kodein {
        bind<Preferences>() with eagerSingleton { PreferencesImpl(this@MainApplication) }
    }

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())

        val preferences:Preferences by kodein.instance()
        SugarContext.init(applicationContext)

        if (preferences.firstLaunch) {
            createDefaultData()
            preferences.firstLaunch = false
        }
    }

    private fun createDefaultData() {
        SchemaGenerator(this).createDatabase(SugarDb(this).db)
        ConnectionHistory("test tcp connection", "http://google.com", 80, TCP_CLIENT).save()
        ConnectionHistory("test udp connection", "127.0.0.1", 1055, UDP_CLIENT).save()
        ConnectionHistory("test ping", "http://google.com/", 80, PING).save()
    }
}
