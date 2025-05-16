package com.appinsnap.aishrm.ui.fragments.attendancehistory.models

import com.google.gson.annotations.SerializedName


data class AttendanceHistoryResponseModel(
    @SerializedName("body")
    var body: List<Body> = listOf(),
    @SerializedName("statusCode")
    var statusCode: String = "",
    @SerializedName("statusMessage")
    var statusMessage: String = "",
    @SerializedName("traceId")
    var traceId: String = ""

)