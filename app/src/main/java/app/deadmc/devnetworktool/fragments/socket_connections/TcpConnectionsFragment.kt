package app.deadmc.devnetworktool.fragments.socket_connections


import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.constants.TCP_CLIENT
import app.deadmc.devnetworktool.extensions.asyncSave
import app.deadmc.devnetworktool.helpers.portFromString
import app.deadmc.devnetworktool.interfaces.views.ConnectionsView
import app.deadmc.devnetworktool.models.ConnectionHistory
import app.deadmc.devnetworktool.presenters.ConnectionsPresenter
import app.deadmc.devnetworktool.presenters.TcpConnectionsPresenter
import com.arellomobile.mvp.presenter.InjectPresenter

import kotlinx.android.synthetic.main.dialog_add_connection.view.*

open class TcpConnectionsFragment : ConnectionsFragment(), ConnectionsView {

    @InjectPresenter
    open lateinit var presenter: TcpConnectionsPresenter

    override fun getPresenter(): ConnectionsPresenter {
        return presenter
    }

    override fun onResume() {
        super.onResume()
        activity?.setTitle(R.string.tcp_client)
    }

    override fun collectConnectionHistory():ConnectionHistory {
        val connectionHistory = ConnectionHistory()
        if (alertDialog?.isShowing == true) {
            connectionHistory.ipAddress = alertView.editTextIpAddress.text.toString()
            connectionHistory.name = alertView.editTextName.text.toString()
            connectionHistory.port = portFromString(alertView.editTextPort.text.toString())
            connectionHistory.setLastUsageDefault()
            connectionHistory.type = TCP_CLIENT
        }
        return connectionHistory
    }


}