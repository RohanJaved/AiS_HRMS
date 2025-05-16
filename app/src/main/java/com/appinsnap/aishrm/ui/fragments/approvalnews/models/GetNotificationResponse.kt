package com.appinsnap.aishrm.ui.fragments.approvalnews.models

import com.google.gson.annotations.SerializedName

data class GetNotificationResponse(
    @SerializedName("body")
    var body: BodyXX = BodyXX(),
    @SerializedName("statusCode")
    var statusCode: String = "",
    @SerializedName("statusMessage")
    var statusMessage: String = "",
    @SerializedName("traceId")
    var traceId: String = ""

)