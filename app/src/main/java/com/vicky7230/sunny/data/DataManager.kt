package com.vicky7230.sunny.data

import com.vicky7230.sunny.data.db.DbHelper
import com.vicky7230.sunny.data.network.ApiHelper
import com.vicky7230.sunny.data.prefs.PreferencesHelper

/**
 * Created by vicky on 31/12/17.
 */
interface DataManager : ApiHelper, DbHelper, PreferencesHelper