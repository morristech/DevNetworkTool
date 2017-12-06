package app.deadmc.devnetworktool.fragments.socket_connections

import app.deadmc.devnetworktool.constants.DevConsts
import app.deadmc.devnetworktool.helpers.CheckHelper
import app.deadmc.devnetworktool.models.ConnectionHistory
import app.deadmc.devnetworktool.presenters.ConnectionsPresenter
import app.deadmc.devnetworktool.presenters.UdpConnectionsPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.add_connection_layout.view.*

class UdpConnectionsFragment : ConnectionsFragment() {

    @InjectPresenter
    lateinit var presenter: UdpConnectionsPresenter

    override fun getPresenter(): ConnectionsPresenter {
        return presenter
    }

    override fun collectConnectionHistory():ConnectionHistory {
        val connectionHistory = ConnectionHistory()
        if (alertDialog?.isShowing == true) {
            connectionHistory.ipAddress = alertView.editTextIpAddress.text.toString()
            connectionHistory.name = alertView.editTextName.text.toString()
            connectionHistory.port = CheckHelper.portFromString(alertView.editTextPort.text.toString())
            connectionHistory.setLastUsageDefault()
            connectionHistory.type = DevConsts.UDP_CLIENT
        }
        return connectionHistory
    }
}