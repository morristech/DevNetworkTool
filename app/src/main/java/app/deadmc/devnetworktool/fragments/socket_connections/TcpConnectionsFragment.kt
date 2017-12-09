package app.deadmc.devnetworktool.fragments.socket_connections


import app.deadmc.devnetworktool.constants.TCP_CLIENT
import app.deadmc.devnetworktool.helpers.CheckHelper
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

    override fun collectConnectionHistory():ConnectionHistory {
        val connectionHistory = ConnectionHistory()
        if (alertDialog?.isShowing == true) {
            connectionHistory.ipAddress = alertView.editTextIpAddress.text.toString()
            connectionHistory.name = alertView.editTextName.text.toString()
            connectionHistory.port = CheckHelper.portFromString(alertView.editTextPort.text.toString())
            connectionHistory.setLastUsageDefault()
            connectionHistory.type = TCP_CLIENT
        }
        return connectionHistory
    }


}