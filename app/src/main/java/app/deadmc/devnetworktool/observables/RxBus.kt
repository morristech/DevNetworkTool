package app.deadmc.devnetworktool.observables

import com.jakewharton.rxrelay2.PublishRelay





/**
 * Created by adanilov on 13.02.2018.
 */
object RxBus {
    private val busSubject = PublishRelay.create<Any>().toSerialized()

    fun post(event: Any) {
        busSubject.accept(event)
    }

}