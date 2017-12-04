package app.deadmc.devnetworktool.fragments.rest

import android.util.Log
import app.deadmc.devnetworktool.constants.DevConsts
import app.deadmc.devnetworktool.fragments.UrlsFragment
import app.deadmc.devnetworktool.fragments.socket_connections.ConnectionsFragment
import app.deadmc.devnetworktool.helpers.CheckHelper
import app.deadmc.devnetworktool.interfaces.ConnectionsView
import app.deadmc.devnetworktool.modules.ConnectionHistory
import app.deadmc.devnetworktool.presenters.ConnectionsPresenter
import app.deadmc.devnetworktool.presenters.PingConnectionsPresenter
import app.deadmc.devnetworktool.presenters.RestConnectionsPresenter
import app.deadmc.devnetworktool.presenters.TcpConnectionsPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.add_url_layout.view.*

/**
 * Created by DEADMC on 11/26/2017.
 */
class RestConnectionsFragment : UrlsFragment(), ConnectionsView {

    @InjectPresenter
    lateinit var presenter: RestConnectionsPresenter

    override fun getPresenter(): ConnectionsPresenter {
        return presenter
    }

    override fun collectConnectionHistory(): ConnectionHistory {
        val connectionHistory = ConnectionHistory()
        if (alertDialog?.isShowing == true) {
            connectionHistory.ipAddress = alertView.urlEditText.text.toString()
            connectionHistory.name = alertView.urlEditText.text.toString()
            connectionHistory.port = 80
            connectionHistory.setLastUsageDefault()
            connectionHistory.type = DevConsts.REST
        }
        return connectionHistory
    }


}