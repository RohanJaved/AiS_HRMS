package com.appinsnap.aishrm.ui.activities.mainactivity.newpassword.models

import com.google.gson.annotations.SerializedName

data class PasswordUpdateResponseModel(
    @SerializedName("body")
    var body: List<String> = listOf(),
    @SerializedName("statusCode")
    var statusCode: String="",
    @SerializedName("statusMessage")
    var statusMessage: String="",
    @SerializedName("traceId")
    var traceId: String=""
)