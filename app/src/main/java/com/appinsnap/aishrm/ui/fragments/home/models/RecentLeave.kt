package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class RecentLeave(
    @SerializedName("appliedDate")
    val appliedDate: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("leaveType")
    val leaveType: String,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("status")
    val status: String
)