package com.hotta.hoho

import android.app.Application
import android.content.Context
import com.hotta.hoho.utils.AppPreferences
import timber.log.Timber

class App : Application() {
    init {
        instance = this
    }


    companion object {
        private var instance: App? = null

        fun context(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)

    }


}


