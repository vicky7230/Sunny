package com.vicky7230.sunny.ui.weather

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.vicky7230.sunny.R
import com.vicky7230.sunny.ui.about.AboutActivity
import com.vicky7230.sunny.ui.add_city.AddCityActivity
import com.vicky7230.sunny.ui.base.BaseActivity
import com.vicky7230.sunny.ui.base.BaseFragment
import com.vicky7230.sunny.ui.saved_cities.SavedCitiesActivity
import com.vicky7230.sunny.ui.weather.city.CityWeatherFragment
import com.vicky7230.sunny.ui.weather.currentLocation.CurrentLocationWeatherFragment
import com.vicky7230.sunny.utils.ThemeUtils
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import javax.inject.Inject


class WeatherActivity : BaseActivity(), OnSuccessListener<LocationSettingsResponse>,
    OnFailureListener, HasSupportFragmentInjector {

    private val REQUEST_REMOVE_CITY = 666
    private val REQUEST_CHECK_SETTINGS = 888
    private val REQUEST_ADD_CITY = 777
    private val LOCATION_PERMISSION_REQUEST = 999

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var viewPagerAdapter: ViewPagerAdapter
    @Inject
    lateinit var compositeDisposable: CompositeDisposable
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var weatherViewModel: WeatherViewModel

    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherViewModel = ViewModelProviders.of(
            this@WeatherActivity,
            viewModelFactory
        )[WeatherViewModel::class.java]

        init()
    }

    private fun init() {
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.title = ""

        if (ThemeUtils.isNight())
            changeRings()

        animateSunAndMoon()

        add_city_button.setOnClickListener {
            if (viewPagerAdapter.count < 5)
                startActivityForResult(
                    AddCityActivity.getStartIntent(this@WeatherActivity),
                    REQUEST_ADD_CITY
                )
            else {
                Snackbar.make(
                    container,
                    "You cannot add more than 4 cities. Click Remove to remove cities.",
                    Snackbar.LENGTH_LONG
                )
                    .setActionTextColor(Color.parseColor("#eccd30"))
                    .setAction("REMOVE") {
                        startActivityForResult(
                            SavedCitiesActivity.getStartIntent(this@WeatherActivity),
                            REQUEST_REMOVE_CITY
                        )
                    }
                    .show()
            }
        }

        view_pager.offscreenPageLimit = 4
        view_pager.adapter = viewPagerAdapter

        setupViewPager()

    }

    private fun changeRings() {

        rings.setBackgroundResource(R.drawable.circular_rings_dark)

        sun_and_moon.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))

        add_city_button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#403C48"))

    }

    private fun animateSunAndMoon() {

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        val animation = ObjectAnimator.ofFloat(sun_and_moon, "translationX", width.toFloat())
        animation.duration = 5000
        animation.repeatCount = ValueAnimator.INFINITE
        animation.repeatMode = ValueAnimator.REVERSE
        animation.start()
    }

    private fun setupViewPager() {

        viewPagerAdapter.removeFragments()
        viewPagerAdapter.addFragment(CurrentLocationWeatherFragment.newInstance())

        compositeDisposable.add(
            Observable.defer { Observable.just(weatherViewModel.getSavedCities()) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ cityList ->
                    cityList.forEach {
                        viewPagerAdapter.addFragment(CityWeatherFragment.newInstance(it.cityName))
                    }
                }, {
                    Timber.e(it)
                    showError(it.localizedMessage)
                })
        )

    }

    public override fun onResume() {
        super.onResume()
        if (hasPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        ) {
            checkLocationSettings()
        } else {
            requestPermissionsSafely(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                , LOCATION_PERMISSION_REQUEST
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.size == 2 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                checkLocationSettings()
            } else
                showError("Permission Denied.")

        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkLocationSettings() {

        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationRequest?.let {
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(it)
                .setAlwaysShow(true)
            val client: SettingsClient = LocationServices.getSettingsClient(this)
            val task: Task<LocationSettingsResponse> =
                client.checkLocationSettings(builder?.build())

            task.addOnSuccessListener(this@WeatherActivity)
            task.addOnFailureListener(this@WeatherActivity)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onSuccess(locationSettingsResponse: LocationSettingsResponse?) {

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                Timber.d("Location : ${locationResult.locations[0].latitude}, ${locationResult.locations[0].longitude}")
                //Post to CurrentLocationWeatherFragment
                EventBus.getDefault().post(locationResult.locations[0])
            }
        }

        fusedLocationClient = FusedLocationProviderClient(this@WeatherActivity)
        fusedLocationClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    override fun onFailure(exception: Exception) {
        if (exception is ResolvableApiException) {
            try {
                exception.startResolutionForResult(this@WeatherActivity, REQUEST_CHECK_SETTINGS)
            } catch (sendEx: IntentSender.SendIntentException) {
                Timber.e(sendEx)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {

            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> Timber.d("User agreed to make required location settings changes.")

                Activity.RESULT_CANCELED -> {
                    checkLocationSettings()
                }
            }

            REQUEST_ADD_CITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    setupViewPager()
                }
            }

            REQUEST_REMOVE_CITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    setupViewPager()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentDispatchingAndroidInjector
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_weather_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.saved_cities -> {
                startActivityForResult(
                    SavedCitiesActivity.getStartIntent(this@WeatherActivity),
                    REQUEST_REMOVE_CITY
                )
                true
            }
            R.id.about -> {
                startActivity(AboutActivity.getStartIntent(this@WeatherActivity))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
