package app.deadmc.devnetworktool.interfaces.views

import com.arellomobile.mvp.MvpView

interface FullView : MvpView {
    fun hide()
    fun setResult(stringId:Int, text:String)
    fun setResultWebView(stringId: Int, text: String, url:String)
}