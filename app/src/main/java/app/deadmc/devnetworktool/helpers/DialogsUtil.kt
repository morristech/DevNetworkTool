package app.deadmc.devnetworktool.helpers

import android.app.Activity
import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.EditText
import android.widget.Spinner
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.presenters.SettingsPresenter
import app.deadmc.devnetworktool.shared_preferences.DevPreferences

inline fun showTimeoutDialog(checkActivityIsFinishing:Boolean, activity: Activity, settingsPresenter:SettingsPresenter,currentDialog:AlertDialog,callback: ()-> Unit) {
    if (checkActivityIsFinishing)
        return
    val alertDialogBuilder = AlertDialog.Builder(activity, R.style.AppTheme_Dialog_Alert)
    val alertView = activity.layoutInflater.inflate(R.layout.dialog_choose_timeout, null)
    alertDialogBuilder.setView(alertView)
    val timeoutEditText = alertView?.findViewById<EditText>(R.id.timeoutEditText)
    timeoutEditText?.setText(DevPreferences.restTimeoutAmount.toString())
    val timeoutUnitSpinner = alertView?.findViewById<Spinner>(R.id.timeoutUnitSpinner)

    alertDialogBuilder.setOnDismissListener {
        settingsPresenter.closeDialog()
    }
    alertDialogBuilder.setPositiveButton(R.string.edit,{ _, _ ->
        timeoutEditText?.let {
            if (!it.text.toString().isBlank())
                DevPreferences.restTimeoutAmount = it.text.toString().toLong()
        }

        settingsPresenter.closeDialog()
    })
    currentDialog = alertDialogBuilder.create()
    currentDialog?.show()
}