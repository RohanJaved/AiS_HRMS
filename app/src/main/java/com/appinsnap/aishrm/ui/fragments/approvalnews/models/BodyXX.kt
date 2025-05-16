package com.appinsnap.aishrm.ui.fragments.approvalnews.models

import com.google.gson.annotations.SerializedName

data class BodyXX(
    @SerializedName("dayNotification")
    var dayNotification: List<DayNotification> = listOf(),

    @SerializedName("monthNotification")
    var monthNotification: List<MonthNotification> = listOf(),

    @SerializedName("weekNotification")
    var weekNotification: List<WeekNotification> = listOf()

)