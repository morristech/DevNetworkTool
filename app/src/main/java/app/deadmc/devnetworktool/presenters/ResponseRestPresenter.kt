package app.deadmc.devnetworktool.presenters

import android.util.Log
import app.deadmc.devnetworktool.events.RestResponseEvent
import app.deadmc.devnetworktool.interfaces.views.ResponseRestView
import app.deadmc.devnetworktool.models.ResponseDev
import app.deadmc.devnetworktool.observables.RxBus
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable

class ResponseRestPresenter : BasePresenter<ResponseRestView>() {
    lateinit var observer : Observer<Any>
    var compositeDisposabie: CompositeDisposable = CompositeDisposable()

    fun initObserver() {
        compositeDisposabie.add(RxBus.subscribe {
            if (it is RestResponseEvent) {
                setResponse(it.responseDev)
                Log.e(TAG,"works")
            }
        })
    }


    fun setResponse(responseDev: ResponseDev) {
        viewState.setResponse(responseDev)
    }
}