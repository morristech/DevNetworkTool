package app.deadmc.devnetworktool.presenters

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable

open class BasePresenter <T : MvpView> : MvpPresenter <T>() {
    val TAG = javaClass.simpleName
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}