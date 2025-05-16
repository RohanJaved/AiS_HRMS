package com.appinsnap.aishrm.ui.fragments.latesitting.models

import com.google.gson.annotations.SerializedName

data class LateSittingStatusRequest(
    @SerializedName("employee_ID")
    val employee_ID: String
)