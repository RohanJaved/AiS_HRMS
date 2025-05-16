package com.appinsnap.aishrm.ui.activities.otp.models

import com.google.gson.annotations.SerializedName

data class OTPResponseModel(
    @SerializedName("body")
    var body: BodyX = BodyX(),
    @SerializedName("statusCode")
    var statusCode: String="",
    @SerializedName("statusMessage")
    var statusMessage: String="",
    @SerializedName("traceId")
    var traceId: String=""
)