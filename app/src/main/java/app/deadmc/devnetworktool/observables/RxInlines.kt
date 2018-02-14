package app.deadmc.devnetworktool.observables

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

inline fun getObserver(crossinline onNextLambda: (value: Any) -> Unit): Observer<Any> {
    return object : Observer<Any> {
        override fun onError(e: Throwable?) {
        }

        override fun onSubscribe(d: Disposable?) {
        }

        override fun onComplete() {
        }

        override fun onNext(value: Any) {
            onNextLambda(value)
        }

    }
}