package com.vicky7230.sunny.data.prefs

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.vicky7230.sunny.di.ApplicationContext
import com.vicky7230.sunny.utils.AppConstants.CITY
import javax.inject.Inject

class AppPreferencesHelper @Inject constructor(@ApplicationContext context: Context) :
    PreferencesHelper {

    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    override fun setCity(city: String) {
        sharedPreferences.edit().putString(CITY, city).apply()
    }

    override fun getCity(): String? {
        return sharedPreferences.getString(CITY, "")
    }

}
