package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class
DashboardDataRequestModel(
    @SerializedName("employeeID")
    val employeeID: String,
    @SerializedName("Email")
    var Email:String
)