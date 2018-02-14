package app.deadmc.devnetworktool.presenters

import app.deadmc.devnetworktool.events.RestResponseEvent
import app.deadmc.devnetworktool.interfaces.views.RestResponseView
import app.deadmc.devnetworktool.models.ResponseDev
import app.deadmc.devnetworktool.observables.RxBus
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class ResponseRestPresenter : BasePresenter<RestResponseView>() {
    override fun initObserver() {
        compositeDisposable.add(RxBus.subscribe {
            if (it is RestResponseEvent) {
                setResponse(it.responseDev)
            }
        })
    }

    fun setResponse(responseDev: ResponseDev) {
        viewState.setResponse(responseDev)
    }
}