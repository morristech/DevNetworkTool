package app.deadmc.devnetworktool.presenters

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
            if (it is RestResponseEvent) {
                viewState.slideViewPager(1)
                viewState.hideProgress()
            }

            if (it is RestRequestEvent) {
                viewState.showProgress()
            }
        })
    }
}