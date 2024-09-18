package it.hypernext.modacenter.fidelity

import android.app.Application

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppInitializer.onApplicationStart()
    }
}