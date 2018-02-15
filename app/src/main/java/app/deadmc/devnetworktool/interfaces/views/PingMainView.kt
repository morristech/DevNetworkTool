package app.deadmc.devnetworktool.interfaces.views

import com.arellomobile.mvp.MvpView

interface PingMainView : MvpView {
    fun setStartButtonOn()
    fun setStartButtonOff()
    fun showProgress()
    fun hideProgress()
}