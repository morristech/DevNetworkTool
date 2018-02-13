package app.deadmc.devnetworktool.observables

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

inline fun <T>getObserver(crossinline onNextLambda:(value:T)->Unit): Observer<T> {
    return object: Observer<T> {
        override fun onError(e: Throwable?) {
        }

        override fun onSubscribe(d: Disposable?) {
        }

        override fun onComplete() {
        }

        override fun onNext(value: T) {
            onNextLambda(value)
        }

    }
}