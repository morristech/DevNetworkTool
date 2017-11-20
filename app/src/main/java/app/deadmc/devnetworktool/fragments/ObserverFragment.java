package app.deadmc.devnetworktool.fragments;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by adanilov on 13.03.2017.
 */

public class ObserverFragment extends ParentFragment {
    protected Observable observable;
    protected Observer observer;
    protected Class className;

    protected <T> void createObserver(Class<T> type) {
        className = type.getClass();
        observer = new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(T value) {
                handleResult(value);
            }

            @Override
            public void onError(Throwable e) {
                handleError(e);
            }

            @Override
            public void onComplete() {

            }
        };
    }

    protected void handleError(Throwable e) {
        Log.e("error",Log.getStackTraceString(e));
    }

    protected void handleResult(Object value) {
    }

    protected void unsubscribe() {
        if (observable != null)
            observable.unsubscribeOn(Schedulers.io());
    }

    @Override
    public void onDestroy() {
        unsubscribe();
        super.onDestroy();
    }




}
