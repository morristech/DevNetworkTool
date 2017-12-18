package app.deadmc.devnetworktool.presenters

import app.deadmc.devnetworktool.interfaces.views.RestDialogsView
import app.deadmc.devnetworktool.models.KeyValueModel
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class RestDialogsPresenter : BasePresenter<RestDialogsView>() {

    var keyValueModel = KeyValueModel()

    fun showDialogForHeader(element:KeyValueModel, position:Int = -1) {
        viewState.showDialogForHeader(element,position)
    }

    fun showDialogForRequest(element:KeyValueModel, position:Int = -1) {
        viewState.showDialogForRequest(element,position)
    }

    fun hideDialog() {
        viewState.hideDialog()
    }
}