package com.vicky7230.sunny.data.db

import com.vicky7230.sunny.data.db.room.model.City
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Created by vicky on 31/12/17.
 */
interface DbHelper {
    fun deleteCity(city: String): Int

    fun insertCity(city: City): Long

    fun getCities(): List<City>
}