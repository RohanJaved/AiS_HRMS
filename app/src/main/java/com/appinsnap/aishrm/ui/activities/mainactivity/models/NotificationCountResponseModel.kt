package com.appinsnap.aishrm.ui.activities.mainactivity.models

import com.google.gson.annotations.SerializedName

data class NotificationCountResponseModel(
    @SerializedName("body")
    var body: Body = Body(),
    @SerializedName("statusCode")
    var statusCode: String="",
    @SerializedName("statusMessage")
    var statusMessage: String="",
    @SerializedName("traceId")
    var traceId: String=""
)