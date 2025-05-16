package com.appinsnap.aishrm.ui.fragments.inappnotification.models


import com.google.gson.annotations.SerializedName

data class ApprovalResponse(
    @SerializedName("body")
    val body: List<String>,
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("statusMessage")
    val statusMessage: String,
    @SerializedName("traceId")
    val traceId: String
)