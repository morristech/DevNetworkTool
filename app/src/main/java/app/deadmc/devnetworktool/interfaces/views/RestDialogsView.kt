package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.KeyValueModel
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface RestDialogsView: MvpView {
    fun showDialogForHeader(keyValueModel: KeyValueModel, position: Int = -1)
    fun showDialogForRequest(keyValueModel: KeyValueModel, position: Int = -1)
    fun hideDialog()
}
