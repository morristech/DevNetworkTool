package app.deadmc.devnetworktool.presenters

import android.util.Log
import app.deadmc.devnetworktool.interfaces.views.PingView
import app.deadmc.devnetworktool.interfaces.views.SettingsView
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class SettingsPresenter : BasePresenter<SettingsView>() {
    fun showRestTimeoutDialog() {
        viewState.showRestTimeoutDialog()
    }

    fun closeDialog() {
        viewState.closeDialog()
    }
}