package com.appinsnap.aishrm.ui.fragments.inappnotification.models


import com.appinsnap.aishrm.ui.fragments.approvalnews.models.NotificationBody
import com.google.gson.annotations.SerializedName

data class InAppResponse(
    @SerializedName("body")
    val body: List<NotificationBody>,
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("statusMessage")
    val statusMessage: String,
    @SerializedName("traceId")
    val traceId: String
)