package app.deadmc.devnetworktool.ui.presentation.presenters

import android.util.Log
import app.deadmc.devnetworktool.events.RestResponseEvent
import app.deadmc.devnetworktool.ui.presentation.views.RestResponseView
import app.deadmc.devnetworktool.data.models.ResponseDev
import app.deadmc.devnetworktool.observables.RxBus
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class RestResponsePresenter : BasePresenter<RestResponseView>() {
    override fun initObserver() {
        compositeDisposable.add(RxBus.subscribe {
            if (it is RestResponseEvent) {
                setResponse(it.responseDev)
            }
        })
    }

    fun setResponse(responseDev: ResponseDev) {
        if (responseDev.error == null) {
            Log.e(TAG,"setSuccessResponse")
            viewState.setSuccessResponse(responseDev)
        } else {
            Log.e(TAG,"setErrorResponse")
            viewState.setErrorResponse(responseDev)
        }
    }
}