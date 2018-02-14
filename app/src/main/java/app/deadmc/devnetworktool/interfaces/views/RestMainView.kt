package app.deadmc.devnetworktool.interfaces.views

import com.arellomobile.mvp.MvpView

interface RestMainView :MvpView {
    fun showProgress()
    fun hideProgress()
    fun slideViewPager(position: Int)
}