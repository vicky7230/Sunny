package com.vicky7230.sunny.di.module

import com.vicky7230.sunny.ui.add_city.AddCityActivity
import com.vicky7230.sunny.ui.add_city.AddCityModule
import com.vicky7230.sunny.ui.saved_cities.SavedCitiesActivity
import com.vicky7230.sunny.ui.saved_cities.SavedCitiesModule
import com.vicky7230.sunny.ui.weather.FragmentProvider
import com.vicky7230.sunny.ui.weather.WeatherActivity
import com.vicky7230.sunny.ui.weather.WeatherActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by vicky on 1/1/18.
 */
@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [(WeatherActivityModule::class), (FragmentProvider::class)])
    abstract fun bindWeatherActivity(): WeatherActivity

    @ContributesAndroidInjector(modules = [(AddCityModule::class)])
    abstract fun bindAddCityActivity(): AddCityActivity

    @ContributesAndroidInjector(modules = [(SavedCitiesModule::class)])
    abstract fun bindSavedCitiesActivity(): SavedCitiesActivity

}