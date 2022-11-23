package com.mom2b.androidApp.application

import android.app.Application
import androidx.databinding.DataBindingUtil
import com.mom2b.androidApp.BuildConfig
import com.mom2b.androidApp.binding.FragmentDataBindingComponent
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        DataBindingUtil.setDefaultComponent(FragmentDataBindingComponent())
    }
}