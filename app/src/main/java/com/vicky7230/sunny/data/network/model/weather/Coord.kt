package com.vicky7230.sunny.data.network.model.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Coord(

    @SerializedName("lon")
    @Expose
    var lon: Double? = null,
    @SerializedName("lat")
    @Expose
    var lat: Double? = null

)
