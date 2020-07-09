package com.leopard.demo

import android.app.Application
import com.leopard.network.NetworkService
import kotlin.properties.Delegates

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        NetworkService.init("www.baidu.com")
    }

    companion object {
        private var instance: App by Delegates.notNull()
        fun instance() = instance
    }


}