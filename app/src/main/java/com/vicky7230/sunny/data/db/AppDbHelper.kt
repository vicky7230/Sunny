package com.vicky7230.sunny.data.db

import com.vicky7230.sunny.data.db.room.AppDatabase
import com.vicky7230.sunny.data.db.room.model.City
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by vicky on 31/12/17.
 */
class AppDbHelper @Inject
constructor(private val appDatabase: AppDatabase) : DbHelper {

    override fun deleteCity(city: String): Int {
        return appDatabase.cityDao().deleteCity(city)
    }

    override fun insertCity(city: City): Long {
        return appDatabase.cityDao().insertCity(city)
    }

    override fun getCities(): List<City> {
        return appDatabase.cityDao().getCities()
    }
}