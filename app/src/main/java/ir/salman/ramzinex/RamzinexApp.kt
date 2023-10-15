package ir.salman.ramzinex

import android.annotation.SuppressLint
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RamzinexApp : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var INSTANCE: RamzinexApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}