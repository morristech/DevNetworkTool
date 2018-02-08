package app.deadmc.devnetworktool.fragments

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.interfaces.views.SettingsView
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
        return myFragmentView
    }

    private fun initRest() {
        myFragmentView.disableSslSwitchCompat.isChecked = DevPreferences.disableSsl
        myFragmentView.disableSslSwitchCompat.setOnCheckedChangeListener({ _, isChecked ->
            DevPreferences.disableSsl = isChecked
        })

        myFragmentView.timeoutLayout.setOnClickListener {
            settingsPresenter.showRestTimeoutDialog()
        }
    }

    private fun initTcpUdp() {
        myFragmentView.currentTcpUdpEncodintTextView.text = DevPreferences.tcpUdpEncoding
    }

    override fun showRestTimeoutDialog() {
        Log.e(TAG,"checkActivityIsFinishing() "+checkActivityIsFinishing())
        if (checkActivityIsFinishing())
            return
        val alertDialogBuilder = AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
        val alertView = activity.layoutInflater.inflate(R.layout.dialog_choose_timeout, null)
        alertDialogBuilder.setView(alertView)
        val timeoutEditText = alertView?.findViewById<EditText>(R.id.timeoutEditText)
        timeoutEditText?.setText(DevPreferences.restTimeoutAmount.toString())
        val timeoutUnitSpinner = alertView?.findViewById<Spinner>(R.id.timeoutUnitSpinner)

        alertDialogBuilder.setOnDismissListener {
            settingsPresenter.closeDialog()
        }
        alertDialogBuilder.setPositiveButton(R.string.edit,{_, _ ->
            timeoutEditText?.let {
                if (!it.text.toString().isBlank())
                    DevPreferences.restTimeoutAmount = it.text.toString().toLong()
            }

            settingsPresenter.closeDialog()
        })
        currentDialog = alertDialogBuilder.create()
        currentDialog?.show()
    }

    override fun closeDialog() {
        currentDialog?.dismiss()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        currentDialog?.setOnDismissListener(null)
        currentDialog?.dismiss()
    }

}
