package com.vicky7230.sunny.data.db.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "cities", indices = [Index(value = ["city_name"], unique = true)])
data class City(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "city_name")
    var cityName: String
)