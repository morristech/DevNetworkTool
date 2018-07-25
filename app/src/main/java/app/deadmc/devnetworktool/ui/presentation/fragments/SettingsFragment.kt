package app.deadmc.devnetworktool.ui.presentation.fragments

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.utils.showSpinnerDialog
import app.deadmc.devnetworktool.utils.showTimeoutDialog
import app.deadmc.devnetworktool.ui.presentation.views.SettingsView
import app.deadmc.devnetworktool.ui.presentation.presenters.BasePresenter
import app.deadmc.devnetworktool.ui.presentation.presenters.SettingsPresenter
import app.deadmc.devnetworktool.data.shared_preferences.PreferencesImpl
import app.deadmc.devnetworktool.extensions.hideKeyboard
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_settings.view.*


class SettingsFragment : BaseFragment(), SettingsView {

    @InjectPresenter
    lateinit var settingsPresenter: SettingsPresenter

    var currentDialog:AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater.inflate(R.layout.fragment_settings, container, false)
        activity?.setTitle(R.string.settings)
        initTcpUdp()
        initRest()
        initPing()
        return myFragmentView
    }

    override fun getPresenter(): BasePresenter<*> {
        return settingsPresenter
    }

    private fun initRest() {
        myFragmentView.disableSslSwitchCompat.isChecked = preferences.disableSsl
        myFragmentView.disableSslSwitchCompat.setOnCheckedChangeListener({ _, isChecked ->
            preferences.disableSsl = isChecked
        })

        myFragmentView.restTimeoutTextView.text = getString(R.string.value_ms, preferences.restTimeoutAmount)
        myFragmentView.restTimeoutLayout.setOnClickListener {
            settingsPresenter.showRestTimeoutDialog()
        }
    }

    private fun initTcpUdp() {
        myFragmentView.currentTcpUdpEncodingTextView.text = preferences.tcpUdpEncoding
        myFragmentView.tcpTimeoutTextView.text = getString(R.string.value_ms, preferences.tcpTimeoutAmount)
        myFragmentView.tcpTimeoutLayout.setOnClickListener {
            settingsPresenter.showTcpTimeoutDialog()
        }
        myFragmentView.tcpUdpEncodingLayout.setOnClickListener {
            settingsPresenter.showTcpEncodingDialog()
        }
    }

    private fun initPing() {
        myFragmentView.pingTimeoutTextView.text = getString(R.string.value_ms, preferences.pingDelay)
        myFragmentView.pingTimeoutLayout.setOnClickListener {
            settingsPresenter.showPingTimeoutDialog()
        }
    }

    override fun showRestTimeoutDialog() {
        currentDialog = showTimeoutDialog(checkActivityIsFinishing(),activity!!,settingsPresenter, preferences.restTimeoutAmount.toString(), {
            preferences.restTimeoutAmount = it.toLong()
            myFragmentView.restTimeoutTextView.text = getString(R.string.value_ms,it)
        })
    }

    override fun showTcpTimeoutDialog() {
        currentDialog = showTimeoutDialog(checkActivityIsFinishing(),activity!!,settingsPresenter, preferences.tcpTimeoutAmount.toString(), {
            preferences.tcpTimeoutAmount = it
            myFragmentView.tcpTimeoutTextView.text = getString(R.string.value_ms,it)
        })
    }

    override fun showTcpEncodingDialog() {
        currentDialog = showSpinnerDialog(checkActivityIsFinishing(),activity!!,settingsPresenter, preferences.tcpUdpEncoding, {
            preferences.tcpUdpEncoding = it
            myFragmentView.currentTcpUdpEncodingTextView.text = it
        })
    }

    override fun showPingTimeoutDialog() {
        currentDialog = showTimeoutDialog(checkActivityIsFinishing(),activity!!,settingsPresenter, preferences.pingDelay.toString(), {
            preferences.pingDelay = it
            myFragmentView.pingTimeoutTextView.text = getString(R.string.value_ms,it)
        })
    }



    override fun closeDialog() {
        activity?.hideKeyboard()
        currentDialog?.dismiss()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        activity?.hideKeyboard()
        currentDialog?.setOnDismissListener(null)
        currentDialog?.dismiss()
    }

}
