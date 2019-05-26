package com.vicky7230.sunny.ui.weather

import dagger.Module
import dagger.Provides

@Module
class WeatherActivityModule {

    @Provides
    fun provideViewPagerAdapter(weatherActivity: WeatherActivity): ViewPagerAdapter {
        return ViewPagerAdapter(weatherActivity.supportFragmentManager)
    }
}