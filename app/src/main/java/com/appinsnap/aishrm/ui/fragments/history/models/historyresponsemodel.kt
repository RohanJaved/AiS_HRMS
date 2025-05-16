package com.appinsnap.aishrm.ui.fragments.history.models

import com.google.gson.annotations.SerializedName

data class historyresponsemodel(
    @SerializedName("body")
    var body: List<Body> = emptyList(),

    @SerializedName("statusCode")
    var statusCode: String = "",

    @SerializedName("statusMessage")
    var statusMessage: String = "",

    @SerializedName("traceId")
    var traceId: String = ""

)