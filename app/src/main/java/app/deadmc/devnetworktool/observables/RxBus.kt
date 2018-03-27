package app.deadmc.devnetworktool.observables

import android.util.Log
import com.crashlytics.android.Crashlytics
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

object RxBus {
    val TAG = RxBus.javaClass.simpleName
    private val busSubject = PublishRelay.create<Any>().toSerialized()

    fun subscribe(lambda: (value: Any) -> Unit): Disposable {
        return busSubject.subscribeOn(
                AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
            lambda(it)
        }, {
            Log.e(TAG,Log.getStackTraceString(it))
            Crashlytics.logException(it)
        })
    }

    fun post(event: Any) {
        Log.e(TAG,event.javaClass.simpleName)
        busSubject.accept(event)
    }
}