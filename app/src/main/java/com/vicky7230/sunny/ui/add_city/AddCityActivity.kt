package com.vicky7230.sunny.ui.add_city

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
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
import kotlinx.android.synthetic.main.activity_add_city.*
import timber.log.Timber
import javax.inject.Inject

class AddCityActivity : BaseActivity(), CityListAdapter.Callback {

    @Inject
    lateinit var compositeDisposable: CompositeDisposable
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var cityListAdapter: CityListAdapter

    private lateinit var allCityArrayList: MutableList<String>
    private lateinit var suggestionsArrayList: MutableList<String>
    private lateinit var addCityViewModel: AddCityViewModel

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, AddCityActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_city)

        addCityViewModel = ViewModelProviders.of(
            this,
            viewModelFactory
        )[AddCityViewModel::class.java]

        cityListAdapter.setCallback(this@AddCityActivity)

        init()
    }

    private fun init() {

        city_list.layoutManager = LinearLayoutManager(this@AddCityActivity)
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

        showLoading()

        compositeDisposable.add(
            Observable.defer { Observable.just(addCityViewModel.addCity(city)) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setResult(Activity.RESULT_OK)
                    finish()
                }, {
                    hideLoading()
                    if (it is SQLiteConstraintException) {
                        showError("This city is already added.")
                    } else {
                        showError(it.localizedMessage)
                    }
                    Timber.e(it)
                })
        )
    }


    override fun onDestroy() {
        hideLoading()
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
