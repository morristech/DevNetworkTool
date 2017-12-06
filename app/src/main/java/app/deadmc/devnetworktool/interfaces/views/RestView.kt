package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.ResponseDev
import app.deadmc.devnetworktool.models.RestRequestHistory
import com.arellomobile.mvp.MvpView

/**
 * Created by DEADMC on 11/29/2017.
 */
interface RestView:MvpView {
    fun setResponse(responseDev: ResponseDev)
    fun loadRequestHistory(restRequestHistory: RestRequestHistory)
}