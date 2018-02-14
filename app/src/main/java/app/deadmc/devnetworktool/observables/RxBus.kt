package app.deadmc.devnetworktool.observables

import android.util.Log
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.disposables.Disposable

object RxBus {
    val TAG = RxBus.javaClass.simpleName
    private val busSubject = PublishRelay.create<Any>()

    fun subscribeWithError(lambda:(value:Any)->Unit, error:(value:Throwable)->Unit):Disposable {
        return busSubject.subscribe( {
            lambda(it)
        },{
            lambda(it)
        })
    }
    fun subscribe(lambda:(value:Any)->Unit):Disposable {

        return busSubject.subscribe {
            lambda(it)
        }
    }

    fun post(event: Any) {
        Log.e(TAG,"post event "+event)
        busSubject.accept(event)
    }

}