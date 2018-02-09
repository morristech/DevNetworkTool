package app.deadmc.devnetworktool.helpers

import android.app.Activity
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.extensions.hideKeyboard
import app.deadmc.devnetworktool.presenters.SettingsPresenter
import app.deadmc.devnetworktool.shared_preferences.DevPreferences


inline fun showTimeoutDialog(checkActivityIsFinishing:Boolean, activity: Activity, settingsPresenter:SettingsPresenter,currentValue:String,crossinline callback: (timeout:Int)-> Unit): AlertDialog? {
    if (checkActivityIsFinishing)
        return null
    val alertDialogBuilder = AlertDialog.Builder(activity, R.style.AppTheme_Dialog_Alert)
    val alertView = activity.layoutInflater.inflate(R.layout.dialog_choose_timeout, null)
    alertDialogBuilder.setView(alertView)
    val timeoutEditText = alertView?.findViewById<EditText>(R.id.timeoutEditText)
    timeoutEditText?.setText(currentValue)

    alertDialogBuilder.setOnDismissListener {
        settingsPresenter.closeDialog()
    }
    alertDialogBuilder.setPositiveButton(R.string.edit,{ _, _ ->
        timeoutEditText?.let {
            if (!it.text.toString().isBlank())
                callback(it.text.toString().toInt())
        }
        activity.hideKeyboard()
        settingsPresenter.closeDialog()
    })
    val currentDialog = alertDialogBuilder.create()
    currentDialog?.show()
    return currentDialog
}

inline fun showSpinnerDialog(checkActivityIsFinishing:Boolean, activity: Activity, settingsPresenter:SettingsPresenter,currentValue:String,crossinline callback: (value:String)-> Unit): AlertDialog? {
    if (checkActivityIsFinishing)
        return null
    val alertDialogBuilder = AlertDialog.Builder(activity, R.style.AppTheme_Dialog_Alert)
    val alertView = activity.layoutInflater.inflate(R.layout.dialog_choose_spinner, null)
    alertDialogBuilder.setView(alertView)
    val spinner = alertView?.findViewById<Spinner>(R.id.spinner)
    val adapter = ArrayAdapter.createFromResource(activity,
            R.array.encodings, android.R.layout.simple_spinner_item)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner?.adapter = adapter
    spinner?.setSelection(adapter.getPosition(DevPreferences.tcpUdpEncoding))

    alertDialogBuilder.setOnDismissListener {
        settingsPresenter.closeDialog()
    }
    alertDialogBuilder.setPositiveButton(R.string.edit,{ _, _ ->
        spinner?.let {
            callback(spinner.selectedItem.toString())
        }
        activity.hideKeyboard()
        settingsPresenter.closeDialog()
    })
    val currentDialog = alertDialogBuilder.create()
    currentDialog?.show()
    return currentDialog
}

