package com.vicky7230.sunny.data.network.model.forecast

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Weather(

    @SerializedName("id")
    @Expose
    var id: Long? = null,
    @SerializedName("main")
    @Expose
    var main: String? = null,
    @SerializedName("description")
    @Expose
    var description: String? = null,
    @SerializedName("icon")
    @Expose
    var icon: String? = null

)
