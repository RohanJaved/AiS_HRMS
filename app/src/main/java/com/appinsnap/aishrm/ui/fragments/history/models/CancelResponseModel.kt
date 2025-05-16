package com.appinsnap.aishrm.ui.fragments.history.models

import com.google.gson.annotations.SerializedName

data class CancelResponseModel(
    @SerializedName("body")
    var body: BodyX = BodyX(),

    @SerializedName("statusCode")
    var statusCode: String = "",

    @SerializedName("statusMessage")
    var statusMessage: String = "",

    @SerializedName("traceId")
    var traceId: String = ""

)