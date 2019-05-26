package com.vicky7230.sunny.ui.saved_cities

import androidx.lifecycle.ViewModel
import com.vicky7230.sunny.data.DataManager
import com.vicky7230.sunny.data.db.room.model.City
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class SavedCitiesViewModel @Inject constructor(
    private val dataManager: DataManager
) : ViewModel() {

    fun getSavedCities(): List<City> {
        return dataManager.getCities()
    }

    fun removeCity(city: String): Int {
        return dataManager.deleteCity(city)
    }

}