package com.vicky7230.sunny.data

import com.vicky7230.sunny.data.db.AppDbHelper
import com.vicky7230.sunny.data.db.room.model.City
import com.vicky7230.sunny.data.network.AppApiHelper
import com.vicky7230.sunny.data.network.model.forecast.Forecast
import com.vicky7230.sunny.data.network.model.weather.CurrentWeather
import com.vicky7230.sunny.data.prefs.AppPreferencesHelper
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by vicky on 31/12/17.
 */
class AppDataManager @Inject
constructor(
    private val appApiHelper: AppApiHelper,
    private val appDbHelper: AppDbHelper,
    private val appPreferencesHelper: AppPreferencesHelper
) : DataManager {


    override fun getCurrentLocationWeather(
        lat: String,
        lon: String,
        appId: String,
        units: String
    ): Observable<CurrentWeather> {
        return appApiHelper.getCurrentLocationWeather(lat, lon, appId, units)
    }

    override fun getCityWeather(
        cityName: String,
        appId: String,
        units: String
    ): Observable<CurrentWeather> {
        return appApiHelper.getCityWeather(cityName, appId, units)
    }

    override fun getCurrentLocationForecast(
        lat: String,
        lon: String,
        appId: String,
        units: String,
        count: String
    ): Observable<Forecast> {
        return appApiHelper.getCurrentLocationForecast(lat, lon, appId, units, count)
    }

    override fun getCityForecast(
        cityName: String,
        appId: String,
        units: String,
        count: String
    ): Observable<Forecast> {
        return appApiHelper.getCityForecast(cityName, appId, units, count)
    }

    override fun deleteCity(city: String): Int {
        return appDbHelper.deleteCity(city)
    }

    override fun insertCity(city: City): Long {
        return appDbHelper.insertCity(city)
    }

    override fun getCities(): List<City> {
        return appDbHelper.getCities()
    }


    override fun getCity(): String? {
        return appPreferencesHelper.getCity()
    }

    override fun setCity(city: String) {
        appPreferencesHelper.setCity(city)
    }
}
