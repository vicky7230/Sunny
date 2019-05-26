package com.vicky7230.sunny.data.network.model.forecast

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class City(

    @SerializedName("id")
    @Expose
    var id: Long? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("coord")
    @Expose
    var coord: Coord? = null,
    @SerializedName("country")
    @Expose
    var country: String? = null,
    @SerializedName("population")
    @Expose
    var population: Long? = null

)
