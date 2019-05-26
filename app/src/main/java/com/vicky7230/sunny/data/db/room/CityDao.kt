package com.vicky7230.sunny.data.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vicky7230.sunny.data.db.room.model.City

/**
 * Created by vicky on 1/3/18.
 */
@Dao
interface CityDao {

    @Query("DELETE FROM cities WHERE city_name =:city ")
    fun deleteCity(city: String): Int

    @Insert
    fun insertCity(city: City): Long

    @Query("SELECT * FROM cities")
    fun getCities(): List<City>
}