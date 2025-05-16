package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class WeeklyAttendanceResponseModel(
    @SerializedName("body")
    val body: BodyXXX,
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("statusMessage")
    val statusMessage: String,
    @SerializedName("traceId")
    val traceId: String
)