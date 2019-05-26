package com.vicky7230.sunny.ui.weather.currentLocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mikepenz.iconics.view.IconicsTextView
import com.vicky7230.sunny.R
import com.vicky7230.sunny.data.network.model.forecast.Forecast
import com.vicky7230.sunny.data.network.model.weather.CurrentWeather
import com.vicky7230.sunny.ui.base.BaseFragment
import com.vicky7230.sunny.utils.CommonUtils
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_current_location_weather.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class CurrentLocationWeatherFragment : BaseFragment() {

    val DEGREE = "\u00b0"

    @Inject
    lateinit var compositeDisposable: CompositeDisposable
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var currentLocationWeatherViewModel: CurrentLocationWeatherViewModel

    private val sourceDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    private val destinationDateFormat = SimpleDateFormat("hh a\nEEE, d MMM", Locale.ENGLISH)

    companion object {
        fun newInstance() = CurrentLocationWeatherFragment()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_current_location_weather, container, false)

        currentLocationWeatherViewModel = ViewModelProviders.of(
            this,
            viewModelFactory
        )[CurrentLocationWeatherViewModel::class.java]

        return view
    }

    override fun setUp(view: View) {

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLatLonUpdate(location: Location) {
        Timber.d("❤️ Got Location : ${location.latitude}, ${location.longitude}")

        compositeDisposable.add(
            currentLocationWeatherViewModel.getWeatherData(
                location.latitude.toString(),
                location.longitude.toString()
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    displayCurrentWeather(it)
                }, {
                    Timber.e(it)
                    showError(it.localizedMessage)
                })
        )

        compositeDisposable.add(
            currentLocationWeatherViewModel.getForecastData(
                location.latitude.toString(),
                location.longitude.toString()
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    displayForecast(it)
                }, {
                    Timber.e(it)
                    showError(it.localizedMessage)
                })
        )
    }

    @SuppressLint("SetTextI18n")
    private fun displayCurrentWeather(currentWeather: CurrentWeather) {

        time_icon.text = "${currentWeather.dt?.let {
            CommonUtils.getTimeFromUnixTimeStamp(
                it
            )
        }}"

        temp.text = "${currentWeather.main?.temp.toString()}${DEGREE}C"

        location.text = "${currentWeather.name}, ${currentWeather.sys?.country}"

        weather.text = currentWeather.weather?.get(0)?.description

        temp_high.text = "H " + currentWeather.main?.tempMax.toString()
        temp_low.text = "  L " + currentWeather.main?.tempMin.toString()

        wind.text = "Winds : ${currentWeather.wind?.speed.toString()} m/s"

        if (currentWeather.weather?.get(0)?.icon.equals("01d"))
            weather_icon.text = "{met_sun}"
        else if (currentWeather.weather?.get(0)?.icon.equals("01n"))
            weather_icon.text = "{met_moon_inv}"
        else if (currentWeather.weather?.get(0)?.icon.equals("02d"))
            weather_icon.text = "{met_cloud_sun}"
        else if (currentWeather.weather?.get(0)?.icon.equals("02n"))
            weather_icon.text = "{met_cloud_moon_inv}"
        else if (currentWeather.weather?.get(0)?.icon.equals("03d"))
            weather_icon.text = "{met_cloud}"
        else if (currentWeather.weather?.get(0)?.icon.equals("03n"))
            weather_icon.text = "{met_cloud_inv}"
        else if (currentWeather.weather?.get(0)?.icon.equals("04d"))
            weather_icon.text = "{met_clouds}"
        else if (currentWeather.weather?.get(0)?.icon.equals("04n"))
            weather_icon.text = "{met_clouds_inv}"
        else if (currentWeather.weather?.get(0)?.icon.equals("09d"))
            weather_icon.text = "{met_rain}"
        else if (currentWeather.weather?.get(0)?.icon.equals("09n"))
            weather_icon.text = "{met_rain_inv}"
        else if (currentWeather.weather?.get(0)?.icon.equals("10d"))
            weather_icon.text = "{met_windy_rain}"
        else if (currentWeather.weather?.get(0)?.icon.equals("10n"))
            weather_icon.text = "{met_windy_rain_inv}"
        else if (currentWeather.weather?.get(0)?.icon.equals("11d"))
            weather_icon.text = "{met_clouds_flash}"
        else if (currentWeather.weather?.get(0)?.icon.equals("11n"))
            weather_icon.text = "{met_clouds_flash_inv}"
        else if (currentWeather.weather?.get(0)?.icon.equals("13d"))
            weather_icon.text = "{met_snow}"
        else if (currentWeather.weather?.get(0)?.icon.equals("13n"))
            weather_icon.text = "{met_snow_inv}"
        else if (currentWeather.weather?.get(0)?.icon.equals("50d") ||
            currentWeather.weather?.get(0)?.icon.equals("50n")
        )
            weather_icon.text = "{met_fog}"

    }

    @SuppressLint("SetTextI18n")
    private fun displayForecast(forecast: Forecast) {

        var i = 1
        for (item in forecast.list!!) {

            val timeTextView = view?.findViewById<View>(
                resources.getIdentifier(
                    "forecast_time_$i",
                    "id",
                    activity?.packageName
                )
            ) as IconicsTextView

            try {
                timeTextView.text = "{ion_android_time}  " + destinationDateFormat.format(
                    sourceDateFormat.parse(
                        item.dtTxt
                    )
                )
            } catch (e: ParseException) {

                Timber.e("Error parsing date : $e")
            }

            val weatherIcon = view?.findViewById<View>(
                resources.getIdentifier(
                    "forecast_icon_$i",
                    "id",
                    activity?.packageName
                )
            ) as IconicsTextView

            if (item.weather?.get(0)?.icon.equals("01d"))
                weatherIcon.text = "{met_sun}"
            else if (item.weather?.get(0)?.icon.equals("01n"))
                weatherIcon.text = "{met_moon}"
            else if (item.weather?.get(0)?.icon.equals("02d"))
                weatherIcon.text = "{met_cloud_sun}"
            else if (item.weather?.get(0)?.icon.equals("02n"))
                weatherIcon.text = "{met_cloud_moon_inv}"
            else if (item.weather?.get(0)?.icon.equals("03d"))
                weatherIcon.text = "{met_cloud}"
            else if (item.weather?.get(0)?.icon.equals("03n"))
                weatherIcon.text = "{met_cloud_inv}"
            else if (item.weather?.get(0)?.icon.equals("04d"))
                weatherIcon.text = "{met_clouds}"
            else if (item.weather?.get(0)?.icon.equals("04n"))
                weatherIcon.text = "{met_clouds_inv}"
            else if (item.weather?.get(0)?.icon.equals("09d"))
                weatherIcon.text = "{met_rain}"
            else if (item.weather?.get(0)?.icon.equals("09n"))
                weatherIcon.text = "{met_rain_inv}"
            else if (item.weather?.get(0)?.icon.equals("10d"))
                weatherIcon.text = "{met_windy_rain}"
            else if (item.weather?.get(0)?.icon.equals("10n"))
                weatherIcon.text = "{met_windy_rain_inv}"
            else if (item.weather?.get(0)?.icon.equals("11d"))
                weatherIcon.text = "{met_clouds_flash}"
            else if (item.weather?.get(0)?.icon.equals("11n"))
                weatherIcon.text = "{met_clouds_flash_inv}"
            else if (item.weather?.get(0)?.icon.equals("13d"))
                weatherIcon.text = "{met_snow}"
            else if (item.weather?.get(0)?.icon.equals("13n"))
                weatherIcon.text = "{met_snow_inv}"
            else if (item.weather?.get(0)?.icon.equals("50d") ||
                item.weather?.get(0)?.icon.equals("50n")
            )
                weatherIcon.text = "{met_fog}"

            val forecastWeatherTextView = view?.findViewById<View>(
                resources.getIdentifier(
                    "forecast_weather_$i",
                    "id",
                    activity?.packageName
                )
            ) as AppCompatTextView

            forecastWeatherTextView.text =
                "" + item.main?.temp.toString() + DEGREE + "C" + "\n" +
                        item.weather?.get(0)?.main

            ++i
        }

    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }

}

