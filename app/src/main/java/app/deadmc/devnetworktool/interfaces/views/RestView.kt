package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.RestRequestHistory
import com.arellomobile.mvp.MvpView

interface RestView:MvpView {
    fun loadRequestHistory(restRequestHistory: RestRequestHistory)
    fun showProgress()
    fun hideProgress()
}