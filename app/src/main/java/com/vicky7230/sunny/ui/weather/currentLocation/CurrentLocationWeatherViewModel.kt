package com.vicky7230.sunny.ui.weather.currentLocation

import androidx.lifecycle.ViewModel
import com.vicky7230.sunny.data.Config
import com.vicky7230.sunny.data.DataManager
import com.vicky7230.sunny.data.network.model.forecast.Forecast
import com.vicky7230.sunny.data.network.model.weather.CurrentWeather
import io.reactivex.Observable
import javax.inject.Inject

class CurrentLocationWeatherViewModel @Inject constructor(
    private val dataManager: DataManager
) : ViewModel() {

    fun getWeatherData(lat: String, lng: String): Observable<CurrentWeather> {
        return dataManager.getCurrentLocationWeather(
            lat,
            lng,
            Config.API_KEY,
            "metric"
        )
    }

    fun getForecastData(lat: String, lng: String): Observable<Forecast> {
        return dataManager.getCurrentLocationForecast(
            lat,
            lng,
            Config.API_KEY,
            "metric",
            "5"
        )
    }
}