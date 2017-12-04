package app.deadmc.devnetworktool.presenters

import android.os.StrictMode
import android.util.Log
import app.deadmc.devnetworktool.interfaces.PingView
import app.deadmc.devnetworktool.interfaces.RestView
import app.deadmc.devnetworktool.modules.KeyValueModel
import app.deadmc.devnetworktool.modules.RestRequestHistory
import app.deadmc.devnetworktool.observables.OkHttpObservable
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by DEADMC on 11/29/2017.
 */
@InjectViewState
class RestPresenter : BasePresenter<RestView>() {
    var currentUrl = ""
    var currentPage = 0
    var currentMethod: String = "GET"
    var headersArrayList: ArrayList<KeyValueModel> = ArrayList()
    var requestArrayList: ArrayList<KeyValueModel> = ArrayList()

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun sendRequest() {
        compositeDisposable.add(OkHttpObservable.getObservable(currentUrl, currentMethod, collectHeaders(), collectRequests())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({
                    viewState.setResponse(it)
                }, {
                    Log.e(TAG, Log.getStackTraceString(it))
                })

        )
    }

    fun loadRestHistory(restRequestHistory: RestRequestHistory) {
        Log.e(TAG,"loadRestHistory currentUrl "+currentUrl)
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