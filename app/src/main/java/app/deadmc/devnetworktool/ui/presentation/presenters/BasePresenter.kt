package app.deadmc.devnetworktool.ui.presentation.presenters

import app.deadmc.devnetworktool.data.shared_preferences.Preferences
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.LateInitKodein
import org.kodein.di.generic.instance

open class BasePresenter <T : MvpView> : MvpPresenter <T>() {
    val TAG = javaClass.simpleName
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val kodein = LateInitKodein()
    val preferences:Preferences by kodein.instance()

    open fun initObserver() {

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}