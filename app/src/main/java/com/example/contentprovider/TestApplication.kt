package com.example.contentprovider

import android.app.Application
import android.util.Log


class TestApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.e("TestApplication", "Application onCreate ${Thread.currentThread().name}")
        if (BuildConfig.DEBUG) {
/*            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyDeath()
                    .build()
            )*/
        }
    }
}
