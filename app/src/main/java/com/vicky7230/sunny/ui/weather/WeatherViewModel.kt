package com.vicky7230.sunny.ui.weather

import androidx.lifecycle.ViewModel
import com.vicky7230.sunny.data.DataManager
import com.vicky7230.sunny.data.db.room.model.City
import io.reactivex.Observable
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val dataManager: DataManager
) : ViewModel() {

    fun getSavedCities(): List<City> {
        return dataManager.getCities()
    }

}