package com.appinsnap.aishrm.ui.fragments.history.models

import com.google.gson.annotations.SerializedName

data class BodyX(
    @SerializedName("statusCode")
    var statusCode: String="",
    @SerializedName("statusMessage")
    var statusMessage: String=""
)