package app.deadmc.devnetworktool.ui.presentation.presenters

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.utils.*


import app.deadmc.devnetworktool.ui.presentation.views.FullView
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class FullViewPresenter : BasePresenter<FullView>() {

    var text: String = ""
    var url:String = ""

    fun setViewByType() {
        val type = getTypeOfString(text)
        viewState.hide()
        when (type) {
            JSON -> {
                viewState.setResult(R.string.json, text)
            }
            XML -> {
                viewState.setResult(R.string.xml, text)
            }
            HTML -> {
                viewState.setResultWebView(R.string.html, text, url)
            }
            UNDEFINED -> {
                viewState.setResult(R.string.undefined, text)
            }
        }
    }
}
