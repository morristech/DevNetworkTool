package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.ResponseDev
import com.arellomobile.mvp.MvpView

interface RestResponseView : MvpView {
    fun setResponse(responseDev: ResponseDev)
}