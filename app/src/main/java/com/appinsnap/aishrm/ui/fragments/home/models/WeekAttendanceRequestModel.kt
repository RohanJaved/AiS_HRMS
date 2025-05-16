package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class WeekAttendanceRequestModel(
    @SerializedName("days")
    val days: Int,
    @SerializedName("employeeID")
    val employeeID: String,
    @SerializedName("date")
    val date: String
)