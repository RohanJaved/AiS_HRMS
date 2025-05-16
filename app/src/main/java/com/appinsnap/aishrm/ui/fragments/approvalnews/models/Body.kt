package com.appinsnap.aishrm.ui.fragments.approvalnews.models

import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("thisDay")
    var thisDay: List<ThisDay> = listOf(),
    @SerializedName("thisMonth")
    var thisMonth: List<ThisMonth> =listOf(),
    @SerializedName("thisWeek")
    var thisWeek: List<ThisWeek> = listOf()
)