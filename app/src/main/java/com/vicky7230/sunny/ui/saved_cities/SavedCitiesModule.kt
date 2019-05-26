package com.vicky7230.sunny.ui.saved_cities

import dagger.Module
import dagger.Provides

@Module
class SavedCitiesModule {

    @Provides
    fun provideViewPagerAdapter(): SavedCityListAdapter {
        return SavedCityListAdapter(arrayListOf())
    }
}