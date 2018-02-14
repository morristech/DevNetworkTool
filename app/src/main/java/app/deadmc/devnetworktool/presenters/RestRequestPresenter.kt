package app.deadmc.devnetworktool.presenters

import android.util.Log
import app.deadmc.devnetworktool.events.RestRequestEvent
import app.deadmc.devnetworktool.events.RestResponseEvent
import app.deadmc.devnetworktool.interfaces.views.RestRequestView
import app.deadmc.devnetworktool.models.KeyValueModel
import app.deadmc.devnetworktool.models.RestRequestHistory
import app.deadmc.devnetworktool.observables.OkHttpObservable
import app.deadmc.devnetworktool.observables.RxBus
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

@InjectViewState
class RestRequestPresenter : BasePresenter<RestRequestView>() {
    var currentUrl = ""
    var currentMethod: String = "GET"
    var headersArrayList: ArrayList<KeyValueModel> = ArrayList()
    var requestArrayList: ArrayList<KeyValueModel> = ArrayList()
    var keyValueModel = KeyValueModel()


    fun showDialogForHeader(element:KeyValueModel, position:Int = -1) {
        viewState.showDialogForHeader(element,position)
    }

    fun showDialogForRequest(element:KeyValueModel, position:Int = -1) {
        viewState.showDialogForRequest(element,position)
    }

    fun hideDialog() {
        viewState.hideDialog()
    }

    fun sendRequest() {
        RxBus.post(RestRequestEvent())
        compositeDisposable.add(OkHttpObservable.getObservable(currentUrl, currentMethod, collectHeaders(), collectRequests())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({
                    RxBus.post(RestResponseEvent(it))
                }, {
                    Log.e(TAG, Log.getStackTraceString(it))
                })

        )
    }

    fun loadRestHistory(restRequestHistory: RestRequestHistory) {
        //viewState.loadRequestHistory(restRequestHistory)
    }

    private fun collectHeaders(): HashMap<String, String> {
        val headers = HashMap<String, String>()
        var value: String
        for (keyValueModel in headersArrayList) {
            if (headers.containsKey(keyValueModel.key)) {
                value = headers[keyValueModel.key] + "," + keyValueModel.value
            } else {
                value = keyValueModel.value
            }
            headers.put(keyValueModel.key, value)
        }
        return headers
    }

    private fun collectRequests(): HashMap<String, String> {
        val requests = HashMap<String, String>()
        for (keyValueModel in requestArrayList) {
            requests.put(keyValueModel.key, keyValueModel.value)
        }

        return requests
    }
}