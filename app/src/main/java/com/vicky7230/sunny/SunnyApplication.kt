package com.vicky7230.sunny

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.LifecycleObserver
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