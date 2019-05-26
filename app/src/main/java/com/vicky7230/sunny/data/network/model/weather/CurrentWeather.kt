package com.vicky7230.sunny.data.network.model.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CurrentWeather(

    @SerializedName("coord")
    @Expose
    var coord: Coord? = null,
    @SerializedName("weather")
    @Expose
    var weather: List<Weather>? = null,
    @SerializedName("base")
    @Expose
    var base: String? = null,
    @SerializedName("main")
    @Expose
    var main: Main? = null,
    @SerializedName("wind")
    @Expose
    var wind: Wind? = null,
    @SerializedName("clouds")
    @Expose
    var clouds: Clouds? = null,
    @SerializedName("dt")
    @Expose
    var dt: Long? = null,
    @SerializedName("sys")
    @Expose
    var sys: Sys? = null,
    @SerializedName("id")
    @Expose
    var id: Long? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("cod")
    @Expose
    var cod: Long? = null

)
