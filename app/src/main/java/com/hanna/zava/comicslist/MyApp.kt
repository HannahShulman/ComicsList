package com.hanna.zava.comicslist

import android.app.Application
import com.hanna.zava.comicslist.di.components.DaggerNetComponent
import com.hanna.zava.comicslist.di.components.NetComponent
import com.hanna.zava.comicslist.di.modules.AppModule
import com.hanna.zava.comicslist.di.modules.NetModule

class MyApp : Application() {

    lateinit var netComponent: NetComponent

    override fun onCreate() {
        super.onCreate()
        netComponent = DaggerNetComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule())
            .build()
    }
}