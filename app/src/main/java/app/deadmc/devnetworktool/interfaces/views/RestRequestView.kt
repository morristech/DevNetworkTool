package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.KeyValueModel
import app.deadmc.devnetworktool.models.RestRequestHistory
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface RestRequestView : MvpView {
    fun showDialogForHeader(keyValueModel: KeyValueModel, position: Int = -1)
    fun showDialogForRequest(keyValueModel: KeyValueModel, position: Int = -1)
    fun loadRestHistory(restRequestHistory: RestRequestHistory)
    fun hideDialog()
}
