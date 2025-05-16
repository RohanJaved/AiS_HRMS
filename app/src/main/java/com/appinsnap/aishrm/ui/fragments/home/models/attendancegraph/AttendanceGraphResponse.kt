package com.appinsnap.aishrm.ui.fragments.home.models.attendancegraph


import com.google.gson.annotations.SerializedName

data class AttendanceGraphResponse(
    @SerializedName("body")
    val body: Body  = Body(),
    @SerializedName("statusCode")
    var statusCode: String? = "",

    @SerializedName("statusMessage")
    var statusMessage: String? = "Success",

    @SerializedName("traceId")
    var traceId: String? = ""
)