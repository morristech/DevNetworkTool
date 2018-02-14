package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.ResponseDev
import com.arellomobile.mvp.MvpView

interface ResponseRestView : MvpView {
    fun setResponse(responseDev: ResponseDev)
}