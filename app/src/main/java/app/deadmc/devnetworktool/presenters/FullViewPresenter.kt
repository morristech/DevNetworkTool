package app.deadmc.devnetworktool.presenters

import android.view.View
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.helpers.FileFormatHelper
import app.deadmc.devnetworktool.helpers.StringHelper
import app.deadmc.devnetworktool.interfaces.views.FullView
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class FullViewPresenter : BasePresenter<FullView>() {

    var text: String = ""
    var url:String = ""

    fun setViewByType() {
        val type = FileFormatHelper.getTypeOfString(text)
        viewState.hide()
        when (type) {
            FileFormatHelper.JSON -> {
                viewState.setResult(R.string.json, text)
            }
            FileFormatHelper.XML -> {
                viewState.setResult(R.string.xml, text)
            }
            FileFormatHelper.HTML -> {
                viewState.setResultWebView(R.string.html, text, url)
            }
            FileFormatHelper.UNDEFINED -> {
                viewState.setResult(R.string.undefined, text)
            }
        }
    }
}
