package com.halan.halantask

import androidx.multidex.MultiDexApplication
import com.halan.halantask.di.networkModule
import com.orhanobut.hawk.Hawk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(listOf(networkModule))
        }
        Hawk.init(this).build()
    }
}