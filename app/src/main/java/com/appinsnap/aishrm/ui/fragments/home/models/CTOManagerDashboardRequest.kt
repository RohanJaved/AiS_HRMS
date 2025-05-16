package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class CTOManagerDashboardRequest(
    @SerializedName("employeeID")
    var employeeID: Int,
    @SerializedName("viewID")
    var viewID: Int,
    @SerializedName("currentDate")
    var currentDate:String
)