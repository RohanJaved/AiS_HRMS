package com.appinsnap.aishrm.ui.fragments.approvalnews.models

import com.google.gson.annotations.SerializedName

data class AcceptRejectResponseModel(
    @SerializedName("body")
    var body: List<String> =  listOf(),
    @SerializedName("statusCode")
    var statusCode: String = "",
    @SerializedName("statusMessage")
    var statusMessage: Any = "",
    @SerializedName("traceId")
    var traceId: String=""
)