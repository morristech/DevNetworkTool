package app.deadmc.devnetworktool.presenters

import android.os.StrictMode
import android.util.Log
import app.deadmc.devnetworktool.interfaces.PingView
import app.deadmc.devnetworktool.interfaces.RestView
import app.deadmc.devnetworktool.observables.OkHttpObservable
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.HashMap

/**
 * Created by DEADMC on 11/29/2017.
 */
@InjectViewState
class RestPresenter : BasePresenter<RestView>() {
    var currentUrl = ""

    private var compositeDisposable:CompositeDisposable = CompositeDisposable()

    fun sendRequest(url: String, requestMethod: String, headers: HashMap<String, String>, body: HashMap<String, String>) {
        Log.e("sendRequest", "url: " + url)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        compositeDisposable.add(OkHttpObservable.getObservable(url, requestMethod, headers, body)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(observer)
        )

    }
}