package app.deadmc.devnetworktool.presenters

import app.deadmc.devnetworktool.interfaces.views.SettingsView
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class SettingsPresenter : BasePresenter<SettingsView>() {
    fun showRestTimeoutDialog() {
        viewState.showRestTimeoutDialog()
    }

    fun showTcpTimeoutDialog() {
        viewState.showTcpTimeoutDialog()
    }

    fun showTcpEncodingDialog() {
        viewState.showTcpEncodingDialog()
    }

    fun showPingTimeoutDialog() {
        viewState.showPingTimeoutDialog()
    }

    fun closeDialog() {
        viewState.closeDialog()
    }
}