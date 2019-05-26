package com.vicky7230.sunny.ui.saved_cities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vicky7230.sunny.R
import com.vicky7230.sunny.ui.base.BaseActivity
import dagger.android.AndroidInjection
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_saved_cities.*
import timber.log.Timber
import javax.inject.Inject

class SavedCitiesActivity : BaseActivity(), SavedCityListAdapter.Callback {

    @Inject
    lateinit var compositeDisposable: CompositeDisposable
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var savedCityListAdapter: SavedCityListAdapter

    private lateinit var savedCitiesViewModel: SavedCitiesViewModel
    private var cityRemoved = false

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, SavedCitiesActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_cities)

        savedCitiesViewModel = ViewModelProviders.of(
            this@SavedCitiesActivity,
            viewModelFactory
        )[SavedCitiesViewModel::class.java]

        savedCityListAdapter.setCallback(this@SavedCitiesActivity)

        init()
    }

    private fun init() {

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        saved_city_list.layoutManager = LinearLayoutManager(this@SavedCitiesActivity)
        saved_city_list.adapter = savedCityListAdapter

        compositeDisposable.add(
            Observable.defer { Observable.just(savedCitiesViewModel.getSavedCities()) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ cityList ->
                    val cityList1 = mutableListOf<String>()
                    cityList.forEach {
                        cityList1.add(it.cityName)
                    }
                    savedCityListAdapter.addCities(cityList1)
                }, {
                    Timber.e(it)
                    showError(it.localizedMessage)
                })
        )
    }

    override fun onRemoveCityClick(city: String) {
        showLoading()
        compositeDisposable.add(
            Observable.defer { Observable.just(savedCitiesViewModel.removeCity(city)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    hideLoading()
                    cityRemoved = true
                }, {
                    hideLoading()
                    Timber.e(it)
                    showError(it.localizedMessage)
                })
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (cityRemoved) {
                    setResult(Activity.RESULT_OK)
                }
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (cityRemoved) {
            setResult(Activity.RESULT_OK)
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

}
