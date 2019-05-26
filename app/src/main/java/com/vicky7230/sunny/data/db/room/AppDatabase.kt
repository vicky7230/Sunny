package com.vicky7230.sunny.data.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vicky7230.sunny.data.db.room.model.City

/**
 * Created by vicky on 31/12/17.
 */
@Database(entities = [City::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}