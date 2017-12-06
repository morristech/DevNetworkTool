package app.deadmc.devnetworktool.observables;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import app.deadmc.devnetworktool.models.ResponseDev;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by adanilov on 13.03.2017.
 */

public class OkHttpObservable {

    public static Observable<ResponseDev> getObservable(final String url, final String requestMethod, final HashMap<String,String> headers, final HashMap<String,String> body) {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Observable observable = Observable.defer(new Callable<ObservableSource<?>>() {
            @Override public Observable<ResponseDev> call() {
                try {
                    Log.e("OkHttpObservable","getObservable called");
                    Request request = buildRequest(url,requestMethod,headers,body);
                    long startTime = System.currentTimeMillis();
                    Response response = okHttpClient.newCall(request).execute();
                    int time = (int)(System.currentTimeMillis() - startTime);
                    int code = response.code();
                    String headers = response.headers().toString();
                    String body = response.body().string();
                    ResponseDev responseDev = new ResponseDev(headers,body,code,time);
                    return Observable.just(responseDev);
                } catch (IOException e) {
                    return Observable.error(e);
                }
            }
        });

        return observable;
    }

    private static Request buildRequest(final String url, final String requestMethod, final HashMap<String,String> headers,final HashMap<String,String> body) {
        //usually Strict mod is bad idea, but have to use it cuz of okhttp/rxJava bug
        Request request = null;
        Headers.Builder headerBuilder = new Headers.Builder();
        for (String headKey:headers.keySet()) {
            headerBuilder.add(headKey,headers.get(headKey));
        }

        FormBody.Builder bodyBuilder = null;
        if (!requestMethod.equals("GET")) {
            bodyBuilder = new FormBody.Builder();
            for (String bodyKey : body.keySet()) {
                bodyBuilder.add(bodyKey, body.get(bodyKey));
            }
            request = new Request.Builder()
                    .url(url)
                    .headers(headerBuilder.build())
                    .method(requestMethod,bodyBuilder.build())
                    .build();

        } else {
            request = new Request.Builder()
                    .url(url+urlForGetRequests(body))
                    .headers(headerBuilder.build())
                    .get()
                    .build();
            return request;
        }

        return request;
    }

    private static String urlForGetRequests(final HashMap<String,String> body) {
        String urlParams = "?";
        boolean isFirst = true;
        for (String bodyKey : body.keySet()) {
            if (isFirst)
                isFirst = false;
            else
                urlParams += "&";
            urlParams += bodyKey+" "+body.get(bodyKey);
        }
        return urlParams;
    }

}
