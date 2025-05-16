package com.appinsnap.aishrm.ui.fragments.history.models

import com.google.gson.annotations.SerializedName

data class historyrequestmodel(
    @SerializedName("employeeID")
    val employeeID: String,
    @SerializedName("fromDate")
    val fromDate: String,
    @SerializedName("toDate")
    val toDate: String
)