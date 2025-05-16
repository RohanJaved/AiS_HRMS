package com.appinsnap.aishrm.ui.fragments.attendancehistory.models

import com.google.gson.annotations.SerializedName


data class Body(
    @SerializedName("checkInTime")
    var checkInTime: String = "",
    @SerializedName("checkOutTime")
    var checkOutTime: String = "",
    @SerializedName("date")
    var date: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("name")
    var name: String = ""

)