package com.vicky7230.sunny.ui.add_city

import dagger.Module
import dagger.Provides

@Module
class AddCityModule {

    @Provides
    fun provideViewPagerAdapter(): CityListAdapter {
        return CityListAdapter(arrayListOf())
    }
}