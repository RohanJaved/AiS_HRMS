package com.appinsnap.aishrm.ui.fragments.home.models.statisticsmodels

import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("onLeaveCount")
    val onLeaveCount: Int,
    @SerializedName("totalAbsent")
    val totalAbsent: Int,
    @SerializedName("totalLate")
    val totalLate: Int,
    @SerializedName("totalPresent")
    val totalPresent: Int
)