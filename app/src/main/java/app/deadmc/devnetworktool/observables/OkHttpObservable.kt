package app.deadmc.devnetworktool.observables

import android.util.Log
import app.deadmc.devnetworktool.models.ResponseDev
import app.deadmc.devnetworktool.shared_preferences.DevPreferences
import io.reactivex.Observable
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object OkHttpObservable {

    fun getObservable(url: String, requestMethod: String, headers: HashMap<String, String>, body: HashMap<String, String>): Observable<ResponseDev> {
        var okHttpClient = getOkHttpBuilder()
                .retryOnConnectionFailure(true)
                .connectTimeout(DevPreferences.restTimeoutAmount,TimeUnit.MILLISECONDS)
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
                val responseDev = ResponseDev(headers, body, code, time,url)
                Observable.just(responseDev)
            } catch (e: Exception) {
                Log.e("OkHttpObservable",Log.getStackTraceString(e))
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

    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                }
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }

            return builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun getOkHttpBuilder():OkHttpClient.Builder {
        if (DevPreferences.disableSsl)
            return getUnsafeOkHttpClient()
        return OkHttpClient.Builder()
    }

}
