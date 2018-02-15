package app.deadmc.devnetworktool.presenters

import android.util.Log
import app.deadmc.devnetworktool.events.RestHistoryEvent
import app.deadmc.devnetworktool.events.RestRequestEvent
import app.deadmc.devnetworktool.events.RestResponseEvent
import app.deadmc.devnetworktool.interfaces.views.RestMainView
import app.deadmc.devnetworktool.observables.RxBus
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class RestMainPresenter : BasePresenter<RestMainView>() {

    var currentPage = 0

    override fun initObserver() {
        compositeDisposable.add(RxBus.subscribe {
            Log.e(TAG,it.javaClass.simpleName)
            if (it is RestResponseEvent) {
                Log.e(TAG,"$viewState")
                viewState.slideViewPager(1)
                viewState.hideProgress()
            }

            if (it is RestRequestEvent) {
                Log.e(TAG,"$viewState")
                viewState.showProgress()
            }

            if (it is RestHistoryEvent) {
                viewState.slideViewPager(0)
            }
        })
    }
}