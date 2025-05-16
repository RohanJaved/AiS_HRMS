package com.appinsnap.aishrm.ui.fragments.approvalnews.models

import com.google.gson.annotations.SerializedName

data class NewsResponseModel(
    @SerializedName("body")
    var body: Body = Body(),
    @SerializedName("statusCode")
    var statusCode: String = "",
    @SerializedName("statusMessage")
    var statusMessage: String = "",
    @SerializedName("traceId")
    var traceId: String = ""

)