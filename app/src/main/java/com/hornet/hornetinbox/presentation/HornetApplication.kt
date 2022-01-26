package com.hornet.hornetinbox.presentation

import android.app.Application
import com.hornet.hornetinbox.DataFetcher
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HornetApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DataFetcher.provideContext(this)
    }
}