package app.deadmc.devnetworktool.interfaces

import com.arellomobile.mvp.MvpView

/**
 * Created by DEADMC on 11/22/2017.
 */
interface PingView : MvpView {
    fun setStartButtonOn()
    fun setStartButtonOff()
}