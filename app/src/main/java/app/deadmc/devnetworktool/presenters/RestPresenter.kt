package app.deadmc.devnetworktool.presenters

import android.util.Log
import app.deadmc.devnetworktool.interfaces.views.RestView
import app.deadmc.devnetworktool.models.KeyValueModel
import app.deadmc.devnetworktool.models.RestRequestHistory
import app.deadmc.devnetworktool.observables.OkHttpObservable
import app.deadmc.devnetworktool.shared_preferences.DevPreferences
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList
import java.util.HashMap

@InjectViewState
class RestPresenter : BasePresenter<RestView>() {
    var currentUrl = ""
    var currentPage = 0
    var currentMethod: String = "GET"
    var headersArrayList: ArrayList<KeyValueModel> = ArrayList()
    var requestArrayList: ArrayList<KeyValueModel> = ArrayList()


    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun sendRequest() {
        viewState.showProgress()
        compositeDisposable.add(OkHttpObservable.getObservable(currentUrl, currentMethod, collectHeaders(), collectRequests())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({
                    viewState.hideProgress()
                    viewState.setResponse(it)
                }, {
                    viewState.hideProgress()
                    Log.e(TAG, Log.getStackTraceString(it))
                })

        )
    }

    fun loadRestHistory(restRequestHistory: RestRequestHistory) {
        viewState.loadRequestHistory(restRequestHistory)
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

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}