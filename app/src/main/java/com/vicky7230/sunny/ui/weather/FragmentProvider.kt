package com.vicky7230.sunny.ui.weather

import com.vicky7230.sunny.ui.weather.city.CityWeatherFragment
import com.vicky7230.sunny.ui.weather.city.CityWeatherModule
import com.vicky7230.sunny.ui.weather.currentLocation.CurrentLocationWeatherFragment
import com.vicky7230.sunny.ui.weather.currentLocation.CurrentLocationWeatherModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentProvider {

    @ContributesAndroidInjector(modules = [(CurrentLocationWeatherModule::class)])
    internal abstract fun provideCurrentLocationWeatherFragment(): CurrentLocationWeatherFragment

    @ContributesAndroidInjector(modules = [(CityWeatherModule::class)])
    internal abstract fun provideCityWeatherModuleFragment(): CityWeatherFragment

}