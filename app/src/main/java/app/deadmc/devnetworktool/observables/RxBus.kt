package app.deadmc.devnetworktool.observables

import com.jakewharton.rxrelay2.PublishRelay

object RxBus {
    private val busSubject = PublishRelay.create<Any>().toSerialized()

    fun post(event: Any) {
        busSubject.accept(event)
    }

}