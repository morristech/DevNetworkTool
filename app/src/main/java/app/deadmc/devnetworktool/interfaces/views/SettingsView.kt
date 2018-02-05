package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.KeyValueModel
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface SettingsView: MvpView {
    fun showRestTimeoutDialog()
}