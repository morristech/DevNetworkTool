package app.deadmc.devnetworktool.interfaces

import app.deadmc.devnetworktool.modules.ResponseDev
import app.deadmc.devnetworktool.modules.RestRequestHistory
import com.arellomobile.mvp.MvpView

/**
 * Created by DEADMC on 11/29/2017.
 */
interface RestView:MvpView {
    fun setResponse(responseDev: ResponseDev)
    fun loadRequestHistory(restRequestHistory: RestRequestHistory)
}