package app.deadmc.devnetworktool.observables

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.disposables.Disposable

object RxBus {
    private val busSubject = PublishRelay.create<Any>().toSerialized()

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
        busSubject.accept(event)
    }

}