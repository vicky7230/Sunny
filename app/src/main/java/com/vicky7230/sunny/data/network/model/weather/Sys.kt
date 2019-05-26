package com.vicky7230.sunny.data.network.model.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Sys(

    @SerializedName("message")
    @Expose
    var message: Double? = null,
    @SerializedName("country")
    @Expose
    var country: String? = null,
    @SerializedName("sunrise")
    @Expose
    var sunrise: Long? = null,
    @SerializedName("sunset")
    @Expose
    var sunset: Long? = null

)
