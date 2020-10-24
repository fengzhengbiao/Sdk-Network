package com.leopard.network

import android.content.Context
import android.os.Environment
import android.util.Log
import com.cash.pinjaman.net.convertor.GsonDataConverterFactory
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object NetworkService {

    val TAG = NetworkService::class.simpleName

    var TIMEOUT_VALUE = 5L


    private val CACHEDIRECTORY = File(Environment.getDataDirectory(), "httpCache")
    private val CACHE = Cache(CACHEDIRECTORY, 10 * 1024 * 1024)
    private lateinit var BASE_URL: String
    lateinit var ctx: Context


    fun createRetrofit() = Retrofit.Builder()
        .client(getOkhttpClient())
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonDataConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun getOkhttpClient() =
        run {
            val client = OkHttpClient.Builder()
                .callTimeout(TIMEOUT_VALUE, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_VALUE, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            INTERCEPTORS.forEach { client.addInterceptor(it) }
            client.cache(CACHE)
                .build()
        }

    val APISERVICE_MAP by lazy {
        ConcurrentHashMap<Class<*>, Any>()
    }

    private val INTERCEPTORS = arrayListOf<Interceptor>()


    inline fun <reified T : Any> getOrCreate(): T = run {
        val clz = T::class.java
        APISERVICE_MAP.getOrPut(clz) { createRetrofit().create<T>(clz) } as T
    }


    fun init(contex:Context,url: String, timeOut: Long = TIMEOUT_VALUE) {
        BASE_URL = url
        ctx=contex.applicationContext
        TIMEOUT_VALUE = timeOut
        Log.i(TAG, "init: base url: $BASE_URL ")
    }

    fun <T : Interceptor> addInterceptor(interceptor: T) {
        INTERCEPTORS.add(interceptor)
    }


}