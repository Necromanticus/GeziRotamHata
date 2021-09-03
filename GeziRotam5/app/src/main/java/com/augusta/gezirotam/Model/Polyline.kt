package com.augusta.gezirotam.Model

import com.google.gson.annotations.SerializedName

data class Polyline(
    @SerializedName("points")
    var points: String?
)