package cz.vcelnicerudna

import android.app.Application
import timber.log.Timber

class AppVcelnice : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}