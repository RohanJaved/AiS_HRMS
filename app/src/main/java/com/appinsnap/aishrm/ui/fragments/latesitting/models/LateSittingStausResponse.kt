package com.appinsnap.aishrm.ui.fragments.latesitting.models

import com.google.gson.annotations.SerializedName

data class LateSittingStausResponse(
    @SerializedName("body")
    val body: BodyX,
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("statusMessage")
    val statusMessage: String,
    @SerializedName("traceId")
    val traceId: String
)