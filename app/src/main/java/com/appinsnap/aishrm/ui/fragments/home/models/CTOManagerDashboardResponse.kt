package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class CTOManagerDashboardResponse(
    @SerializedName("body")
    var body: List<BodyXX>,
    @SerializedName("statusCode")
    var statusCode: String,
    @SerializedName("statusMessage")
    var statusMessage: String,
    @SerializedName("traceId")
    var traceId: String
)