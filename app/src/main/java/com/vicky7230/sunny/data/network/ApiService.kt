package com.vicky7230.sunny.data.network

import com.vicky7230.sunny.data.network.model.forecast.Forecast
import com.vicky7230.sunny.data.network.model.weather.CurrentWeather
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by vicky on 31/12/17.
 */
interface ApiService {

    @GET("weather")
    abstract fun getCurrentLocationWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("APPID") appId: String,
        @Query("units") units: String
    ): Observable<CurrentWeather>

    @GET("weather")
    abstract fun getCityWeather(
        @Query("q") cityName: String,
        @Query("APPID") appId: String,
        @Query("units") units: String
    ): Observable<CurrentWeather>

    @GET("forecast")
    abstract fun getCurrentLocationForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("APPID") appId: String,
        @Query("units") units: String,
        @Query("cnt") count: String
    ): Observable<Forecast>

    @GET("forecast")
    abstract fun getCityForecast(
        @Query("q") cityName: String,
        @Query("APPID") appId: String,
        @Query("units") units: String,
        @Query("cnt") count: String
    ): Observable<Forecast>
}