package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class WeeklyAttendanceresponce(
    @SerializedName("date")
    var date: String,
    @SerializedName("days")
    val days: String,
    @SerializedName("month")
    val month: String,
    @SerializedName("remarks")
    val remarks: String,
    @SerializedName("lastCheckOut")
    var lastCheckOut:String,
    @SerializedName("firstCheckIn")
    var firstCheckIn:String,
    @SerializedName("isReqSubmittedd")
    var isReqSubmittedd:Boolean=false,
    @SerializedName("lateSittingMinutes")
    var lateSittingMinutes:String


)



