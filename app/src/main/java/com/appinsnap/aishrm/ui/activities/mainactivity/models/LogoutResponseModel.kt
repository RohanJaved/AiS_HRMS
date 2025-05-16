package com.appinsnap.aishrm.ui.activities.mainactivity.models

import com.google.gson.annotations.SerializedName

data class LogoutResponseModel(
    @SerializedName("message")
    var message: String="",
    @SerializedName("status")
    var status: Int=-1
)