package com.vicky7230.sunny.data.network.model.forecast

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Sys(

    @SerializedName("pod")
    @Expose
    var pod: String? = null

)
