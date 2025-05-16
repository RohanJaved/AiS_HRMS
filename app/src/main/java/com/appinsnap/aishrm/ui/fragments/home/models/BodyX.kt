package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class BodyX(
    @SerializedName("date")
    var date: String,
    @SerializedName("days")
    var days: String,
    @SerializedName("remarks")
    var remarks: String,
    @SerializedName("month")
    var month :String

)