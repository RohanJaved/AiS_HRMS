package com.appinsnap.aishrm.ui.fragments.home.models
import com.google.gson.annotations.SerializedName

data class AttendanceSummary(
    @SerializedName("remarks")
    val remarks: String,
    @SerializedName("totalCount")
    val totalCount: String
)