package app.deadmc.devnetworktool.presenters

import app.deadmc.devnetworktool.interfaces.views.RestDialogsView
import app.deadmc.devnetworktool.models.KeyValueModel
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class RestDialogsPresenter : BasePresenter<RestDialogsView>() {

    var currentKey = ""
    var currentValue = ""

    fun showDialogForHeader() {
        viewState.showDialogForHeader()
    }
    fun showDialogForRequest() {
        viewState.showDialogForRequest()
    }

    fun showDialogForEditRequest(element:KeyValueModel, position:Int) {
        viewState.showDialogForEditRequest(element,position)
    }

    fun showDialogForEditHeader(element:KeyValueModel, position:Int) {
        viewState.showDialogForEditHeader(element,position)
    }

    fun hideDialog() {
        viewState.hideDialog()
    }
}