package app.deadmc.devnetworktool.activities

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.constants.PING_FRAGMENT
import app.deadmc.devnetworktool.constants.REST_FRAGMENT
import app.deadmc.devnetworktool.constants.WORKING_CONNECTION_FRAGMENT
import app.deadmc.devnetworktool.extensions.hideKeyboard
import app.deadmc.devnetworktool.fragments.SettingsFragment
import app.deadmc.devnetworktool.fragments.ping.MainPingFragment
import app.deadmc.devnetworktool.fragments.ping.PingConnectionsFragment
import app.deadmc.devnetworktool.fragments.rest.MainRestFragment
import app.deadmc.devnetworktool.fragments.socket_connections.TcpConnectionsFragment
import app.deadmc.devnetworktool.fragments.socket_connections.UdpConnectionsFragment
import app.deadmc.devnetworktool.fragments.socket_connections.WorkingConnectionFragment
import app.deadmc.devnetworktool.helpers.startServiceForeground
import app.deadmc.devnetworktool.interfaces.views.MainActivityView
import app.deadmc.devnetworktool.models.ConnectionHistory
import app.deadmc.devnetworktool.presenters.MainPresenter
import app.deadmc.devnetworktool.presenters.WorkingConnectionPresenter
import app.deadmc.devnetworktool.services.ConnectionService
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.Serializable

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, MainActivityView {

    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var mainPresenter: MainPresenter

    var workingConnectionsPresenter: WorkingConnectionPresenter? = null
    var serviceBound = false
    var serviceConnection: ServiceConnection? = null
    var connectionService: ConnectionService? = null
    var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.itemIconTintList = null
        navigationView.setNavigationItemSelectedListener(this)

        if (isServiceRunning(ConnectionService::class.java.name))
            doBindService(null)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        runFragmentDependsOnClickedItem(id)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun runFragmentDependsOnClickedItem(item: Int) {
        if (isServiceRunning(ConnectionService::class.java.name)) {
            stopService()
        }

        val fm = supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }

        when (item) {
            R.id.tcp_client -> {runFragment(TcpConnectionsFragment())}
            R.id.udp_client -> runFragment(UdpConnectionsFragment())
            R.id.ping -> runFragment(PingConnectionsFragment())
            R.id.rest -> runFragment(MainRestFragment())
            R.id.settings -> runFragment(SettingsFragment())
            R.id.exit -> mainPresenter.showDialogExitConnection()
        }

        hideKeyboard()
    }

    override fun runFragmentDependsOnId(id: Int) {
        when (id) {
            WORKING_CONNECTION_FRAGMENT -> runFragment(WorkingConnectionFragment())
        }
    }

    override fun runFragmentDependsOnId(id: Int, serializable: Serializable) {
        when (id) {
            PING_FRAGMENT -> runFragment(MainPingFragment.getInstance(serializable))
            WORKING_CONNECTION_FRAGMENT -> runFragment(WorkingConnectionFragment.getInstance(serializable))
            REST_FRAGMENT -> runFragment(MainRestFragment())
        }
    }

    override fun runFragmentDefault() {
        runFragment(MainRestFragment())
    }

    override fun showDialogExitConnection() {
        if (isServiceRunning(ConnectionService::class.java.name)) {
            val alertDialogBuilder = AlertDialog.Builder(this, R.style.AppTheme_Dialog_Alert)
            alertDialogBuilder.setMessage(R.string.alert_close_connection)
            alertDialogBuilder.setPositiveButton(R.string.yes) { _, _ ->
                stopService()
                onBackPressed()
            }

            alertDialogBuilder.setNegativeButton(R.string.no) { _, _ -> mainPresenter.hideDialogExitConnection() }
            alertDialog = alertDialogBuilder.create()
            alertDialog?.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            alertDialog?.show()
        } else {
            super.onBackPressed()
        }

    }

    override fun hideDialogExitConnection() {
        alertDialog?.dismiss()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.action_settings).isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        super.onPrepareOptionsMenu(menu)
        return true
    }

    override fun doBindService(connectionHistory: ConnectionHistory?) {
        Log.e("doBindService", "service is binded " + serviceBound)
        if (serviceBound)
            return
        val intent = Intent(this,
                ConnectionService::class.java)
        startServiceForeground(this,intent)


        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, binder: IBinder) {
                Log.e("ServiceConnection", "connected")

                connectionService = (binder as ConnectionService.LocalBinder).service

                if (connectionService?.getCurrentClient() == null)
                    connectionService?.initConnection(connectionHistory)
                serviceBound = true
                Log.e("main activity","working presenter == null "+(workingConnectionsPresenter == null))
                Log.e("main activity", "connectionService?.isRunning "+connectionService?.isRunning)
                if (workingConnectionsPresenter == null && connectionService?.isRunning == true)
                    mainPresenter.runFragmentDependsOnId(WORKING_CONNECTION_FRAGMENT)
                connectionService?.workingConnectionPresenter = workingConnectionsPresenter
                if (workingConnectionsPresenter?.currentConnectionHistory == null) {
                    workingConnectionsPresenter?.currentConnectionHistory = connectionService?.connectionHistory
                }
                workingConnectionsPresenter?.currentClient = connectionService?.getCurrentClient()
                Log.e("main activity","second working presenter == null "+(workingConnectionsPresenter == null))
                workingConnectionsPresenter?.successfulCallback()
            }

            override fun onServiceDisconnected(className: ComponentName) {
                Log.e("ServiceConnection", "disconnected")
                connectionService = null
                serviceBound = false
            }
        }

        serviceBound = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun doUnbindService() {
        if (serviceConnection != null) {
            try {
                unbindService(serviceConnection)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            connectionService = null
            serviceBound = false
        }
    }

    override fun setCustomTitle(stringId: Int) {
        toolbar.setTitle(stringId)
        Log.e("setTitleActivity", "" + getString(stringId))
    }

    override fun setCustomTitle(title: String) {
        toolbar.setTitle(title)
        Log.e("setTitleActivity", "" + title)
    }


    override fun stopService() {
        connectionService?.stopService()

        val intent = Intent(this,
                ConnectionService::class.java)
        stopService(intent)
        doUnbindService()
    }

    private fun isServiceRunning(serviceName: String): Boolean {
        var serviceRunning = false
        val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val l = am.getRunningServices(50)
        val i = l.iterator()
        while (i.hasNext()) {
            val runningServiceInfo = i
                    .next() as ActivityManager.RunningServiceInfo

            if (runningServiceInfo.service.className == serviceName) {
                serviceRunning = true
            }
        }
        return serviceRunning
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (isServiceRunning(ConnectionService::class.java.name))
                mainPresenter.showDialogExitConnection()
            else
                super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connectionService?.workingConnectionPresenter = null
        doUnbindService()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mvpDelegate.onSaveInstanceState(outState)
        mvpDelegate.onDetach()
    }

}