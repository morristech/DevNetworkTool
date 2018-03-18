package app.deadmc.devnetworktool.presenters

import android.util.Log
import app.deadmc.devnetworktool.events.RestHistoryEvent
import app.deadmc.devnetworktool.events.RestRequestEvent
import app.deadmc.devnetworktool.events.RestResponseEvent
import app.deadmc.devnetworktool.extensions.deferredSave
import app.deadmc.devnetworktool.helpers.safe
import app.deadmc.devnetworktool.interfaces.views.RestRequestView
import app.deadmc.devnetworktool.models.KeyValueModel
import app.deadmc.devnetworktool.models.ResponseDev
import app.deadmc.devnetworktool.models.RestRequestHistory
import app.deadmc.devnetworktool.observables.OkHttpObservable
import app.deadmc.devnetworktool.observables.RxBus
import app.deadmc.devnetworktool.shared_preferences.DevPreferences
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*
import java.util.concurrent.TimeUnit

@InjectViewState
class RestRequestPresenter : BasePresenter<RestRequestView>() {
    var currentUrl = ""
    var currentMethod: String = "GET"
    var headersArrayList: ArrayList<KeyValueModel> = ArrayList()
    var requestArrayList: ArrayList<KeyValueModel> = ArrayList()
    var keyValueModel = KeyValueModel()

    override fun initObserver() {
        compositeDisposable.add(RxBus.subscribe {
            if (it is RestHistoryEvent) {
                loadRestHistory(it.restRequestHistory)
            }
        })
    }


    fun showDialogForHeader(element: KeyValueModel, position: Int = -1) {
        viewState.showDialogForHeader(element, position)
    }

    fun showDialogForRequest(element: KeyValueModel, position: Int = -1) {
        viewState.showDialogForRequest(element, position)
    }

    fun hideDialog() {
        viewState.hideDialog()
    }

    fun sendRequest() {
        runRestHistoryEventAfterSave()
        Log.e(TAG, "sendRequest")
        compositeDisposable.add(OkHttpObservable.getObservable(currentUrl, currentMethod, collectHeaders(), collectRequests())
                .subscribeOn(Schedulers.io())
                .timeout(DevPreferences.restTimeoutAmount, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn {
                    Log.e(TAG,"onErrorReturn")
                    ResponseDev(delay = DevPreferences.restTimeoutAmount.toInt(), currentUrl = currentUrl,error = it)
                }
                .unsubscribeOn(Schedulers.io())
                .subscribe({
                    Log.e(TAG, "response")
                    RxBus.post(RestResponseEvent(it))
                }, {
                    Log.e(TAG, "error")
                    Log.e(TAG, Log.getStackTraceString(it))
                    RxBus.post(RestResponseEvent( ResponseDev(delay = DevPreferences.restTimeoutAmount.toInt(), currentUrl = currentUrl,error = it)))
                }))
    }

    fun runRestHistoryEventAfterSave() {
        async(UI) {
            val id = RestRequestHistory(currentUrl, currentMethod, headersArrayList, requestArrayList).deferredSave().await()
            RxBus.post(RestRequestEvent(id))
        }
    }

    fun loadRestHistory(restRequestHistory: RestRequestHistory) {
        viewState.loadRestHistory(restRequestHistory)
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