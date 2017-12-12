package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.KeyValueModel
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface RestDialogsView: MvpView {
    fun showDialogForHeader()
    fun showDialogForRequest()
    fun showDialogForEditRequest(keyValueModel: KeyValueModel, position: Int)
    fun showDialogForEditHeader(keyValueModel: KeyValueModel, position: Int)
    fun hideDialog()
}
