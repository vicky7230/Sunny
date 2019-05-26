package com.vicky7230.sunny.ui.weather.city

import androidx.lifecycle.ViewModel
import com.vicky7230.sunny.data.Config
import com.vicky7230.sunny.data.DataManager
import com.vicky7230.sunny.data.network.model.forecast.Forecast
import com.vicky7230.sunny.data.network.model.weather.CurrentWeather
import io.reactivex.Observable
import javax.inject.Inject

class CityWeatherViewModel @Inject constructor(
    private val dataManager: DataManager
) : ViewModel() {

    fun getWeatherData(city: String): Observable<CurrentWeather> {
        return dataManager.getCityWeather(
            city,
            Config.API_KEY,
            "metric"
        )
    }

    fun getForecastData(city: String): Observable<Forecast> {
        return dataManager.getCityForecast(
            city,
            Config.API_KEY,
            "metric",
            "5"
        )
    }

}