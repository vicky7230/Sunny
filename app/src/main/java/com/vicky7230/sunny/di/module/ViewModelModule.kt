package com.vicky7230.sunny.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vicky7230.sunny.ViewModelFactory
import com.vicky7230.sunny.di.ViewModelKey
import com.vicky7230.sunny.ui.add_city.AddCityViewModel
import com.vicky7230.sunny.ui.saved_cities.SavedCitiesViewModel
import com.vicky7230.sunny.ui.weather.WeatherViewModel
import com.vicky7230.sunny.ui.weather.city.CityWeatherViewModel
import com.vicky7230.sunny.ui.weather.currentLocation.CurrentLocationWeatherViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel::class)
    internal abstract fun postWeatherViewModel(viewModel: WeatherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CurrentLocationWeatherViewModel::class)
    internal abstract fun postCurrentLocationWeatherViewModel(currentLocationWeatherViewModel: CurrentLocationWeatherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddCityViewModel::class)
    internal abstract fun postAddCityViewModel(addCityViewModel: AddCityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CityWeatherViewModel::class)
    internal abstract fun postCityWeatherViewModel(cityWeatherViewModel: CityWeatherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SavedCitiesViewModel::class)
    internal abstract fun postSavedCitiesViewModel(savedCitiesViewModel: SavedCitiesViewModel): ViewModel

}