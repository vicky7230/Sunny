package com.vicky7230.sunny.di.component

import com.vicky7230.sunny.di.module.ActivityBindingModule
import com.vicky7230.sunny.di.module.ApplicationModule
import com.vicky7230.sunny.di.module.NetworkModule
import com.vicky7230.sunny.di.module.ViewModelModule
import com.vicky7230.sunny.SunnyApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by vicky on 12/2/18.
 */
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        NetworkModule::class,
        ApplicationModule::class,
        ActivityBindingModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(sunnyApplication: SunnyApplication): Builder

        fun build(): ApplicationComponent
    }

    fun inject(sunnyApplication: SunnyApplication)
}