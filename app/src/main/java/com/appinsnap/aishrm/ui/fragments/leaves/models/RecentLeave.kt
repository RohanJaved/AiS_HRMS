package com.appinsnap.aishrm.ui.fragments.leaves.models

import com.google.gson.annotations.SerializedName

data class RecentLeave(
    @SerializedName("appliedDate")
    var appliedDate: String,
    @SerializedName("endDate")
    var endDate: String,
    @SerializedName("leaveType")
    val leaveType: String,
    @SerializedName("startDate")
    var startDate: String,
    @SerializedName("status")
    val status: String
)