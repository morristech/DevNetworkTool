package app.deadmc.devnetworktool.presenters

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView

open class BasePresenter <T : MvpView> : MvpPresenter <T>() {
    val TAG = javaClass.simpleName
}