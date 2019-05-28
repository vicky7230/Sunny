package com.vicky7230.sunny.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View.*
import android.widget.RemoteViews
import android.widget.Toast
import com.vicky7230.sunny.BuildConfig
import com.vicky7230.sunny.R
import com.vicky7230.sunny.data.Config
import com.vicky7230.sunny.data.network.ApiService
import com.vicky7230.sunny.data.network.model.weather.CurrentWeather
import com.vicky7230.sunny.data.prefs.AppPreferencesHelper
import com.vicky7230.sunny.utils.AppConstants.CITY
import com.vicky7230.sunny.utils.CommonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit


class WeatherWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    companion object {

        val ACTION_WEATHER_WIDGET = "ACTION_WEATHER_WIDGET"

        private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
        }

        private fun getDisableCacheInterceptor(): Interceptor {
            return Interceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("cache-control", "no-cache")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
        }

        private fun getOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .readTimeout(1000, TimeUnit.SECONDS)
                .addInterceptor(getHttpLoggingInterceptor())
                .addInterceptor(getDisableCacheInterceptor())
                .build()
        }

        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            val views = RemoteViews(
                context.packageName,
                R.layout.weather_widget
            )

            val appPreferencesHelper = AppPreferencesHelper(context)
            val intent = Intent(context, WeatherWidget::class.java)
            intent.action = ACTION_WEATHER_WIDGET
            intent.putExtra(CITY, appPreferencesHelper.getCity())
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.refresh_button, pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)

            if (appPreferencesHelper.getCity() != null && appPreferencesHelper.getCity() != "") {
                getWeatherData(appPreferencesHelper.getCity() as String, context)
            }
        }

        @SuppressLint("CheckResult")
        private fun getWeatherData(city: String, context: Context?) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build()

            val apiService = retrofit.create(ApiService::class.java)
            val appWidget = ComponentName(context, WeatherWidget::class.java)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val views = RemoteViews(
                context?.packageName,
                R.layout.weather_widget
            )

            views.setViewVisibility(R.id.temp, INVISIBLE)
            views.setViewVisibility(R.id.weather, INVISIBLE)
            views.setViewVisibility(R.id.location, INVISIBLE)
            views.setViewVisibility(R.id.date, INVISIBLE)
            views.setViewVisibility(R.id.icon, INVISIBLE)
            views.setViewVisibility(R.id.progress, VISIBLE)
            appWidgetManager.updateAppWidget(appWidget, views)

            apiService.getCityWeather(
                city,
                Config.API_KEY,
                "metric"
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ currentWeather ->

                    views.setViewVisibility(R.id.temp, VISIBLE)
                    views.setViewVisibility(R.id.weather, VISIBLE)
                    views.setViewVisibility(R.id.location, VISIBLE)
                    views.setViewVisibility(R.id.date, VISIBLE)
                    views.setViewVisibility(R.id.icon, VISIBLE)
                    views.setViewVisibility(R.id.progress, GONE)
                    appWidgetManager.updateAppWidget(appWidget, views)

                    updateWidgetUI(currentWeather, views, appWidget, appWidgetManager)

                }, {

                    views.setViewVisibility(R.id.temp, INVISIBLE)
                    views.setViewVisibility(R.id.weather, INVISIBLE)
                    views.setViewVisibility(R.id.location, INVISIBLE)
                    views.setViewVisibility(R.id.date, INVISIBLE)
                    views.setViewVisibility(R.id.icon, INVISIBLE)
                    views.setViewVisibility(R.id.progress, GONE)
                    appWidgetManager.updateAppWidget(appWidget, views)

                    Timber.e(it)
                    Toast.makeText(
                        context,
                        "Error : ${it.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                })
        }

        private fun updateWidgetUI(
            currentWeather: CurrentWeather,
            views: RemoteViews,
            appWidget: ComponentName,
            appWidgetManager: AppWidgetManager
        ) {

            views.setTextViewText(
                R.id.temp,
                "${currentWeather.main?.temp.toString()}°C"
            )
            views.setTextViewText(
                R.id.weather,
                currentWeather.weather?.get(0)?.description
            )
            views.setTextViewText(
                R.id.location,
                "${currentWeather.name}, ${currentWeather.sys?.country}"
            )
            views.setTextViewText(R.id.date,
                currentWeather.dt?.let {
                    CommonUtils.getTimeForWidget(
                        it
                    )
                }
            )

            if (currentWeather.weather?.get(0)?.icon.equals("01d"))
                views.setImageViewResource(R.id.icon, R.drawable.met_sun)
            else if (currentWeather.weather?.get(0)?.icon.equals("01n"))
                views.setImageViewResource(R.id.icon, R.drawable.met_moon_inv)
            else if (currentWeather.weather?.get(0)?.icon.equals("02d"))
                views.setImageViewResource(R.id.icon, R.drawable.met_cloud_sun)
            else if (currentWeather.weather?.get(0)?.icon.equals("02n"))
                views.setImageViewResource(R.id.icon, R.drawable.met_cloud_moon_inv)
            else if (currentWeather.weather?.get(0)?.icon.equals("03d"))
                views.setImageViewResource(R.id.icon, R.drawable.met_cloud)
            else if (currentWeather.weather?.get(0)?.icon.equals("03n"))
                views.setImageViewResource(R.id.icon, R.drawable.met_cloud_inv)
            else if (currentWeather.weather?.get(0)?.icon.equals("04d"))
                views.setImageViewResource(R.id.icon, R.drawable.met_clouds)
            else if (currentWeather.weather?.get(0)?.icon.equals("04n"))
                views.setImageViewResource(R.id.icon, R.drawable.met_clouds_inv)
            else if (currentWeather.weather?.get(0)?.icon.equals("09d"))
                views.setImageViewResource(R.id.icon, R.drawable.met_rain)
            else if (currentWeather.weather?.get(0)?.icon.equals("09n"))
                views.setImageViewResource(R.id.icon, R.drawable.met_rain_inv)
            else if (currentWeather.weather?.get(0)?.icon.equals("10d"))
                views.setImageViewResource(R.id.icon, R.drawable.met_windy_rain)
            else if (currentWeather.weather?.get(0)?.icon.equals("10n"))
                views.setImageViewResource(R.id.icon, R.drawable.met_windy_rain_inv)
            else if (currentWeather.weather?.get(0)?.icon.equals("11d"))
                views.setImageViewResource(R.id.icon, R.drawable.met_clouds_flash)
            else if (currentWeather.weather?.get(0)?.icon.equals("11n"))
                views.setImageViewResource(R.id.icon, R.drawable.met_clouds_flash_inv)
            else if (currentWeather.weather?.get(0)?.icon.equals("13d"))
                views.setImageViewResource(R.id.icon, R.drawable.met_snow)
            else if (currentWeather.weather?.get(0)?.icon.equals("13n"))
                views.setImageViewResource(R.id.icon, R.drawable.met_snow_inv)
            else if (currentWeather.weather?.get(0)?.icon.equals("50d") ||
                currentWeather.weather?.get(0)?.icon.equals("50n")
            )
                views.setImageViewResource(R.id.icon, R.drawable.met_fog)

            appWidgetManager.updateAppWidget(appWidget, views)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (ACTION_WEATHER_WIDGET == intent?.action) {
            if (intent.getStringExtra(CITY) != null && intent.getStringExtra(CITY) != "") {
                getWeatherData(intent.getStringExtra(CITY), context)
            }
        }
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
    }
}

