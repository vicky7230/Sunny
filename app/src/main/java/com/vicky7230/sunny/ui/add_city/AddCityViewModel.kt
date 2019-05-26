package com.vicky7230.sunny.ui.add_city

import androidx.lifecycle.ViewModel
import com.vicky7230.sunny.data.DataManager
import com.vicky7230.sunny.data.db.room.model.City
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class AddCityViewModel @Inject constructor(
    private val dataManager: DataManager
) : ViewModel() {

    fun addCity(city: String): Long {
        return dataManager.insertCity(City(cityName = city))
    }
}