package com.appinsnap.aishrm.ui.fragments.attendancehistory.models

import com.google.gson.annotations.SerializedName


data class AttendanceHistoryRequestModel(
    @SerializedName("employeeID")
    val employeeID: String,
    @SerializedName("fromDate")
    val fromDate: String,
    @SerializedName("toDate")
    val toDate: String
)