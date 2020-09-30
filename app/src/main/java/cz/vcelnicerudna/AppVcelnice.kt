package cz.vcelnicerudna

import android.app.Application
import com.google.firebase.ktx.BuildConfig
import timber.log.Timber

@Suppress("unused")
class AppVcelnice : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}