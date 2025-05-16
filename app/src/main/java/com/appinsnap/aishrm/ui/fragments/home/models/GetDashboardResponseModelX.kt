package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class GetDashboardResponseModelX(
    @SerializedName("body")
    val body: BodyXXXXXX,
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("statusMessage")
    val statusMessage: String,
    @SerializedName("traceId")
    val traceId: String
)