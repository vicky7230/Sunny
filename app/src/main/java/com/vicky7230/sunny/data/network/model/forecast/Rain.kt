package com.vicky7230.sunny.data.network.model.forecast

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Rain (

    @SerializedName("3h")
    @Expose
    var _3h:Double? = null

)
