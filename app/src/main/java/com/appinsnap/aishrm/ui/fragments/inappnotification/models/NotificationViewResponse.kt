package com.appinsnap.aishrm.ui.fragments.inappnotification.models

import com.google.gson.annotations.SerializedName

data class NotificationViewResponse(
    @SerializedName("body")
    val body: BodyX,
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("statusMessage")
    val statusMessage: String,
    @SerializedName("traceId")
    val traceId: Any
)