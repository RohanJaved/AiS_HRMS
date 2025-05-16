package com.appinsnap.aishrm.ui.activities.forgotpassword.models

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponseModel(
    @SerializedName("body")
    val body: Body = Body(),
    @SerializedName("statusCode")
    val statusCode: String="",
    @SerializedName("statusMessage")
    val statusMessage: String="",
    @SerializedName("traceId")
    val traceId: String=""
)