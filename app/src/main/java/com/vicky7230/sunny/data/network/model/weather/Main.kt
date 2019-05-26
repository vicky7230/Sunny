package com.vicky7230.sunny.data.network.model.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Main(

    @SerializedName("temp")
    @Expose
    var temp: Double? = null,
    @SerializedName("pressure")
    @Expose
    var pressure: Double? = null,
    @SerializedName("humidity")
    @Expose
    var humidity: Long? = null,
    @SerializedName("temp_min")
    @Expose
    var tempMin: Double? = null,
    @SerializedName("temp_max")
    @Expose
    var tempMax: Double? = null,
    @SerializedName("sea_level")
    @Expose
    var seaLevel: Double? = null,
    @SerializedName("grnd_level")
    @Expose
    var grndLevel: Double? = null

)
