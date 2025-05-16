package com.appinsnap.aishrm.ui.fragments.LeaaveApplication.models

import com.google.gson.annotations.SerializedName

data class LeaveResponseModel(
    @SerializedName("body")
    val body: Any,
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("statusMessage")
    val statusMessage: String,
    @SerializedName("traceId")
    val traceId: String
)