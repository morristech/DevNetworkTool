package app.deadmc.devnetworktool.fragments.ping


import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.constants.PING
import app.deadmc.devnetworktool.fragments.UrlsFragment
import app.deadmc.devnetworktool.interfaces.views.ConnectionsView
import app.deadmc.devnetworktool.models.ConnectionHistory
import app.deadmc.devnetworktool.presenters.ConnectionsPresenter
import app.deadmc.devnetworktool.presenters.PingConnectionsPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.dialog_add_url.view.*

class PingConnectionsFragment : UrlsFragment(), ConnectionsView {

    @InjectPresenter
    lateinit var presenter: PingConnectionsPresenter

    override fun getPresenter(): ConnectionsPresenter {
        return presenter
    }

    override fun onResume() {
        super.onResume()
        activity.setTitle(R.string.ping)
    }

    override fun collectConnectionHistory(): ConnectionHistory {
        val connectionHistory = ConnectionHistory()
        if (alertDialog?.isShowing == true) {
            connectionHistory.ipAddress = alertView.urlEditText.text.toString()
            connectionHistory.name = alertView.urlEditText.text.toString()
            connectionHistory.port = 80
            connectionHistory.setLastUsageDefault()
            connectionHistory.type = PING
        }
        return connectionHistory
    }


}