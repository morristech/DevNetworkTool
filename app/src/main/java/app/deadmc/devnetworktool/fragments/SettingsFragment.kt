package app.deadmc.devnetworktool.fragments

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.extensions.hideKeyboard
import app.deadmc.devnetworktool.helpers.showSpinnerDialog
import app.deadmc.devnetworktool.helpers.showTimeoutDialog
import app.deadmc.devnetworktool.interfaces.views.SettingsView
import app.deadmc.devnetworktool.presenters.BasePresenter
import app.deadmc.devnetworktool.presenters.SettingsPresenter
import app.deadmc.devnetworktool.shared_preferences.DevPreferences
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_settings.view.*


class SettingsFragment : BaseFragment(), SettingsView {

    @InjectPresenter
    lateinit var settingsPresenter: SettingsPresenter

    var currentDialog:AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater!!.inflate(R.layout.fragment_settings, container, false)
        activity.setTitle(R.string.settings)
        initTcpUdp()
        initRest()
        initPing()
        return myFragmentView
    }

    override fun getPresenter(): BasePresenter<*> {
        return settingsPresenter
    }

    private fun initRest() {
        myFragmentView.disableSslSwitchCompat.isChecked = DevPreferences.disableSsl
        myFragmentView.disableSslSwitchCompat.setOnCheckedChangeListener({ _, isChecked ->
            DevPreferences.disableSsl = isChecked
        })

        myFragmentView.restTimeoutTextView.text = getString(R.string.value_ms,DevPreferences.restTimeoutAmount)
        myFragmentView.restTimeoutLayout.setOnClickListener {
            settingsPresenter.showRestTimeoutDialog()
        }
    }

    private fun initTcpUdp() {
        myFragmentView.currentTcpUdpEncodingTextView.text = DevPreferences.tcpUdpEncoding
        myFragmentView.tcpTimeoutTextView.text = getString(R.string.value_ms,DevPreferences.tcpTimeoutAmount)
        myFragmentView.tcpTimeoutLayout.setOnClickListener {
            settingsPresenter.showTcpTimeoutDialog()
        }
        myFragmentView.tcpUdpEncodingLayout.setOnClickListener {
            settingsPresenter.showTcpEncodingDialog()
        }
    }

    private fun initPing() {
        myFragmentView.pingTimeoutTextView.text = getString(R.string.value_ms,DevPreferences.pingDelay)
        myFragmentView.pingTimeoutLayout.setOnClickListener {
            settingsPresenter.showPingTimeoutDialog()
        }
    }

    override fun showRestTimeoutDialog() {
        currentDialog = showTimeoutDialog(checkActivityIsFinishing(),activity,settingsPresenter,DevPreferences.restTimeoutAmount.toString(), {
            DevPreferences.restTimeoutAmount = it.toLong()
            myFragmentView.restTimeoutTextView.text = getString(R.string.value_ms,it)
        })
    }

    override fun showTcpTimeoutDialog() {
        currentDialog = showTimeoutDialog(checkActivityIsFinishing(),activity,settingsPresenter,DevPreferences.tcpTimeoutAmount.toString(), {
            DevPreferences.tcpTimeoutAmount = it
            myFragmentView.tcpTimeoutTextView.text = getString(R.string.value_ms,it)
        })
    }

    override fun showTcpEncodingDialog() {
        currentDialog = showSpinnerDialog(checkActivityIsFinishing(),activity,settingsPresenter,DevPreferences.tcpUdpEncoding, {
            DevPreferences.tcpUdpEncoding = it
            myFragmentView.currentTcpUdpEncodingTextView.text = it
        })
    }

    override fun showPingTimeoutDialog() {
        currentDialog = showTimeoutDialog(checkActivityIsFinishing(),activity,settingsPresenter,DevPreferences.pingDelay.toString(), {
            DevPreferences.pingDelay = it
            myFragmentView.pingTimeoutTextView.text = getString(R.string.value_ms,it)
        })
    }



    override fun closeDialog() {
        activity.hideKeyboard()
        currentDialog?.dismiss()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        activity.hideKeyboard()
        currentDialog?.setOnDismissListener(null)
        currentDialog?.dismiss()
    }

}
