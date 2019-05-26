package com.vicky7230.sunny.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.vicky7230.sunny.data.Config
import com.vicky7230.sunny.SunnyApplication
import com.vicky7230.sunny.data.AppDataManager
import com.vicky7230.sunny.data.DataManager
import com.vicky7230.sunny.data.db.AppDbHelper
import com.vicky7230.sunny.data.db.DbHelper
import com.vicky7230.sunny.data.db.room.AppDatabase
import com.vicky7230.sunny.data.network.ApiHelper
import com.vicky7230.sunny.data.network.AppApiHelper
import com.vicky7230.sunny.di.ApplicationContext
import com.vicky7230.sunny.di.BaseUrl
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

/**
 * Created by vicky on 31/12/17.
 */
@Module
class ApplicationModule {

    @Provides
    @ApplicationContext
    internal fun provideContext(sunnyApplication: SunnyApplication): Context {
        return sunnyApplication.applicationContext
    }

    @Provides
    internal fun provideApplication(sunnyApplication: SunnyApplication): Application {
        return sunnyApplication
    }

    @Provides
    internal fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, Config.DB_NAME).build()
    }

    @Provides
    @BaseUrl
    internal fun provideBaseUrl(): String {
        return Config.BASE_URL
    }

    @Provides
    @Singleton
    internal fun provideDataManager(appDataManager: AppDataManager): DataManager {
        return appDataManager
    }

    @Provides
    @Singleton
    internal fun provideDbHelper(appDbHelper: AppDbHelper): DbHelper {
        return appDbHelper
    }

    @Provides
    @Singleton
    internal fun provideApiHelper(appApiHelper: AppApiHelper): ApiHelper {
        return appApiHelper
    }

    /*@Provides
    @Singleton
    internal fun providePreferencesHelper(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper {
        return appPreferencesHelper
    }*/
}