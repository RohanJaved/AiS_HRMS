package com.appinsnap.aishrm.ui.activities.mainactivity.models

import com.google.gson.annotations.SerializedName

data class LogoutRequestModel(
    @SerializedName("employeeid")
    val employeeid: Int
)