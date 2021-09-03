package com.augusta.gezirotam.Model

import com.augusta.gezirotam.Model.Bounds
import com.augusta.gezirotam.Model.Leg
import com.augusta.gezirotam.Model.OverviewPolyline
import com.google.gson.annotations.SerializedName

data class Route(
    @SerializedName("bounds")
    var bounds: Bounds?,
    @SerializedName("copyrights")
    var copyrights: String?,
    @SerializedName("legs")
    var legs: List<Leg?>?,
    @SerializedName("overview_polyline")
    var overviewPolyline: OverviewPolyline?,
    @SerializedName("summary")
    var summary: String?,
    @SerializedName("warnings")
    var warnings: List<Any?>?,
    @SerializedName("waypoint_order")
    var waypointOrder: List<Any?>?
)