package app.deadmc.devnetworktool.interfaces.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface SettingsView: MvpView {
    fun showRestTimeoutDialog()
    fun showTcpTimeoutDialog()
    fun showTcpEncodingDialog()
    fun showPingTimeoutDialog()
    fun closeDialog()
}