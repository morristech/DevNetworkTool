package app.deadmc.devnetworktool.observables

import android.util.Log

import java.io.IOException
import java.util.HashMap
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

import app.deadmc.devnetworktool.models.ResponseDev
import app.deadmc.devnetworktool.observables.OkHttpObservable.buildRequest
import io.reactivex.Observable
import io.reactivex.ObservableSource
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

object OkHttpObservable {

    fun getObservable(url: String, requestMethod: String, headers: HashMap<String, String>, body: HashMap<String, String>): Observable<ResponseDev> {
        val okHttpClient = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build()

        return Observable.defer<ResponseDev>( {
            try {
                val request = buildRequest(url, requestMethod, headers, body)
                val startTime = System.currentTimeMillis()
                val response = okHttpClient.newCall(request).execute()
                val time = (System.currentTimeMillis() - startTime).toInt()
                val code = response.code()
                val headers = response.headers().toString()
                val body = response.body().string()
                val responseDev = ResponseDev(headers, body, code, time)
                Observable.just(responseDev)
            } catch (e: IOException) {
                Observable.error(e)
            }
        })
    }

    private fun buildRequest(url: String, requestMethod: String, headers: HashMap<String, String>, body: HashMap<String, String>): Request? {
        var request: Request? = null
        val headerBuilder = Headers.Builder()
        for (headKey in headers.keys) {
            headerBuilder.add(headKey, headers[headKey])
        }

        var bodyBuilder: FormBody.Builder? = null
        if (requestMethod != "GET") {
            bodyBuilder = FormBody.Builder()
            for (bodyKey in body.keys) {
                bodyBuilder.add(bodyKey, body[bodyKey])
            }
            request = Request.Builder()
                    .url(url)
                    .headers(headerBuilder.build())
                    .method(requestMethod, bodyBuilder.build())
                    .build()

        } else {
            request = Request.Builder()
                    .url(url + urlForGetRequests(body))
                    .headers(headerBuilder.build())
                    .get()
                    .build()
            return request
        }

        return request
    }

    private fun urlForGetRequests(body: HashMap<String, String>): String {
        var urlParams = "?"
        var isFirst = true
        for (bodyKey in body.keys) {
            if (isFirst)
                isFirst = false
            else
                urlParams += "&"
            urlParams += bodyKey + " " + body[bodyKey]
        }
        return urlParams
    }

}
