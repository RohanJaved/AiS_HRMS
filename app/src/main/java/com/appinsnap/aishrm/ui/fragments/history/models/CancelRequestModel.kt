package com.appinsnap.aishrm.ui.fragments.history.models

import com.google.gson.annotations.SerializedName

data class CancelRequestModel(
    @SerializedName("employeID")
    val employeID: Int,
    @SerializedName("notificationID")
    val notificationID: Int
)