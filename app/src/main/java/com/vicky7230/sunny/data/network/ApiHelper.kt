package com.vicky7230.sunny.data.network

import com.vicky7230.sunny.data.network.model.forecast.Forecast
import com.vicky7230.sunny.data.network.model.weather.CurrentWeather
import io.reactivex.Observable

/**
 * Created by vicky on 31/12/17.
 */
interface ApiHelper {
    abstract fun getCurrentLocationWeather(
        lat: String,
        lon: String,
        appId: String,
        units: String
    ): Observable<CurrentWeather>

    abstract fun getCityWeather(
        cityName: String,
        appId: String,
        units: String
    ): Observable<CurrentWeather>


    abstract fun getCurrentLocationForecast(
        lat: String,
        lon: String,
        appId: String,
        units: String,
        count: String
    ): Observable<Forecast>


    abstract fun getCityForecast(
        cityName: String,
        appId: String,
        units: String,
        count: String
    ): Observable<Forecast>
}