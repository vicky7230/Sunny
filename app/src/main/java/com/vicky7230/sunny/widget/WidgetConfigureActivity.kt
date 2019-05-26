package com.vicky7230.sunny.widget

import android.app.Activity
import android.app.Dialog
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.vicky7230.sunny.BuildConfig
import com.vicky7230.sunny.R
import com.vicky7230.sunny.data.Config
import com.vicky7230.sunny.data.network.ApiService
import com.vicky7230.sunny.data.prefs.AppPreferencesHelper
import com.vicky7230.sunny.ui.add_city.CityListAdapter
import com.vicky7230.sunny.utils.AppConstants.CITY
import com.vicky7230.sunny.utils.CommonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_widget_configure.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit


class WidgetConfigureActivity : AppCompatActivity(), CityListAdapter.Callback {

    private lateinit var allCityArrayList: MutableList<String>
    private lateinit var suggestionsArrayList: MutableList<String>
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var cityListAdapter: CityListAdapter

    private var progressDialog: Dialog? = null

    var mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private var widgetManager: AppWidgetManager? = null
    private var views: RemoteViews? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(RESULT_CANCELED)
        setContentView(R.layout.activity_widget_configure)

        // These steps are seen in the previous examples
        widgetManager = AppWidgetManager.getInstance(this)
        views = RemoteViews(this.packageName, R.layout.weather_widget)

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        init()

    }

    private fun init() {

        compositeDisposable = CompositeDisposable()

        cityListAdapter = CityListAdapter(mutableListOf())
        cityListAdapter.setCallback(this@WidgetConfigureActivity)

        city_list.layoutManager = LinearLayoutManager(this@WidgetConfigureActivity)
        city_list.adapter = cityListAdapter

        val cityArray = resources.getStringArray(R.array.india_top_places)
        allCityArrayList = cityArray.toMutableList()
        suggestionsArrayList = cityArray.toMutableList()

        cityListAdapter.addCities(suggestionsArrayList)

        city_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                suggestionsArrayList.clear()
                for (city in allCityArrayList) {
                    if (city.toLowerCase().startsWith(city_edit_text.text.toString().toLowerCase()))
                        suggestionsArrayList.add(city)
                }
                cityListAdapter.addCities(suggestionsArrayList)
            }
        })

        clear_button.setOnClickListener {
            city_edit_text.text?.clear()
        }
    }

    override fun onCityClick(city: String) {

        val appPreferencesHelper = AppPreferencesHelper(this.applicationContext)
        appPreferencesHelper.setCity(city)

        val retrofit = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getOkHttpClient())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        showLoading()

        compositeDisposable.add(
            apiService.getCityWeather(
                city,
                Config.API_KEY,
                "metric"
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ currentWeather ->

                    hideLoading()

                    views?.setTextViewText(
                        R.id.temp,
                        "${currentWeather.main?.temp.toString()}°C"
                    )
                    views?.setTextViewText(
                        R.id.weather,
                        currentWeather.weather?.get(0)?.description
                    )
                    views?.setTextViewText(
                        R.id.location,
                        "${currentWeather.name}, ${currentWeather.sys?.country}"
                    )
                    views?.setTextViewText(R.id.date,
                        currentWeather.dt?.let {
                            CommonUtils.getTimeForWidget(
                                it
                            )
                        }
                    )

                    if (currentWeather.weather?.get(0)?.icon.equals("01d"))
                        views?.setImageViewResource(R.id.icon, R.drawable.met_sun)
                    else if (currentWeather.weather?.get(0)?.icon.equals("01n"))
                        views?.setImageViewResource(R.id.icon, R.drawable.met_moon_inv)
                    else if (currentWeather.weather?.get(0)?.icon.equals("02d"))
                        views?.setImageViewResource(R.id.icon, R.drawable.met_cloud_sun)
                    else if (currentWeather.weather?.get(0)?.icon.equals("02n"))
                        views?.setImageViewResource(R.id.icon, R.drawable.met_cloud_moon_inv)
                    else if (currentWeather.weather?.get(0)?.icon.equals("03d"))
                        views?.setImageViewResource(R.id.icon, R.drawable.met_cloud)
                    else if (currentWeather.weather?.get(0)?.icon.equals("03n"))
                        views?.setImageViewResource(R.id.icon, R.drawable.met_cloud_inv)
                    else if (currentWeather.weather?.get(0)?.icon.equals("04d"))
                        views?.setImageViewResource(R.id.icon, R.drawable.met_clouds)
                    else if (currentWeather.weather?.get(0)?.icon.equals("04n"))
                        views?.setImageViewResource(R.id.icon, R.drawable.met_clouds_inv)
                    else if (currentWeather.weather?.get(0)?.icon.equals("09d"))
                        views?.setImageViewResource(R.id.icon, R.drawable.met_rain)
                    else if (currentWeather.weather?.get(0)?.icon.equals("09n"))
                        views?.setImageViewResource(R.id.icon, R.drawable.met_rain_inv)
                    else if (currentWeather.weather?.get(0)?.icon.equals("10d"))
                        views?.setImageViewResource(R.id.icon, R.drawable.met_windy_rain)
                    else if (currentWeather.weather?.get(0)?.icon.equals("10n"))
                        views?.setImageViewResource(R.id.icon, R.drawable.met_windy_rain_inv)
                    else if (currentWeather.weather?.get(0)?.icon.equals("11d"))
                        views?.setImageViewResource(R.id.icon, R.drawable.met_clouds_flash)
                    else if (currentWeather.weather?.get(0)?.icon.equals("11n"))
                        views?.setImageViewResource(R.id.icon, R.drawable.met_clouds_flash_inv)
                    else if (currentWeather.weather?.get(0)?.icon.equals("13d"))
                        views?.setImageViewResource(R.id.icon, R.drawable.met_snow)
                    else if (currentWeather.weather?.get(0)?.icon.equals("13n"))
                        views?.setImageViewResource(R.id.icon, R.drawable.met_snow_inv)
                    else if (currentWeather.weather?.get(0)?.icon.equals("50d") ||
                        currentWeather.weather?.get(0)?.icon.equals("50n")
                    )
                        views?.setImageViewResource(R.id.icon, R.drawable.met_fog)

                    val intent = Intent(this@WidgetConfigureActivity, WeatherWidget::class.java)
                    intent.action = WeatherWidget.ACTION_WEATHER_WIDGET
                    intent.putExtra(CITY, city)
                    val pendingIntent = PendingIntent.getBroadcast(
                        this@WidgetConfigureActivity, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    views?.setOnClickPendingIntent(R.id.refresh_button, pendingIntent)

                    widgetManager = AppWidgetManager.getInstance(this@WidgetConfigureActivity)
                    widgetManager?.updateAppWidget(mAppWidgetId, views)

                    val resultValue = Intent()
                    // Set the results as expected from a 'configure activity'.
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
                    setResult(Activity.RESULT_OK, resultValue)
                    finish()

                }, {
                    hideLoading()
                    Timber.e(it)
                    Toast.makeText(
                        this@WidgetConfigureActivity,
                        "Error : ${it.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                })
        )
    }

    private fun showLoading() {
        progressDialog = CommonUtils.showLoadingDialog(this@WidgetConfigureActivity)
    }

    private fun hideLoading() {
        if (progressDialog?.isShowing == true)
            progressDialog?.cancel()
    }

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

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
