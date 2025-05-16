package com.appinsnap.aishrm.ui.activities.otp.models

import com.google.gson.annotations.SerializedName

data class BodyX(
    @SerializedName("result")
    var result: Boolean=false,
    @SerializedName("statusCode")
    var statusCode: Int=-1,
    @SerializedName("statusDesc")
    var statusDesc: String="",
    @SerializedName("userID")
    var userID: String=""
)