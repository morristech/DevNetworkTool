package app.deadmc.devnetworktool.ui.presentation.fragments.socket_connections


import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.constants.UDP_CLIENT
import app.deadmc.devnetworktool.utils.portFromString

import app.deadmc.devnetworktool.data.models.ConnectionHistory
import app.deadmc.devnetworktool.ui.presentation.presenters.ConnectionsPresenter
import app.deadmc.devnetworktool.ui.presentation.presenters.UdpConnectionsPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.dialog_add_connection.view.*

class UdpConnectionsFragment : ConnectionsFragment() {

    @InjectPresenter
    lateinit var presenter: UdpConnectionsPresenter

    override fun getPresenter(): ConnectionsPresenter {
        return presenter
    }

    override fun onResume() {
        super.onResume()
        activity?.setTitle(R.string.udp_client)
    }

    override fun collectConnectionHistory():ConnectionHistory {
        val connectionHistory = ConnectionHistory()
        if (alertDialog?.isShowing == true) {
            connectionHistory.ipAddress = alertView.editTextIpAddress.text.toString()
            connectionHistory.name = alertView.editTextName.text.toString()
            connectionHistory.port = portFromString(alertView.editTextPort.text.toString())
            connectionHistory.setLastUsageDefault()
            connectionHistory.type = UDP_CLIENT
        }
        return connectionHistory
    }
}