package com.vicky7230.sunny

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.vicky7230.sunny.di.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by vicky on 11/2/18.
 */

class SunnyApplication : Application(), HasActivityInjector, LifecycleObserver {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        if (BuildConfig.DEBUG) {
            val requestConfiguration = RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("331F7A5A20DA46701B7C03C1AE7CDF48")).build()
            MobileAds.setRequestConfiguration(requestConfiguration);
        }
        MobileAds.initialize(this) { }

        DaggerApplicationComponent
            .builder()
            .application(this)
            .build()
            .inject(this)

    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityDispatchingAndroidInjector
    }
}