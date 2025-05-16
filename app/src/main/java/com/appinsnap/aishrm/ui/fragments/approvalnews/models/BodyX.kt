package com.appinsnap.aishrm.ui.fragments.approvalnews.models

import com.google.gson.annotations.SerializedName

data class BodyX(
    @SerializedName("employeeID")
    var employeeID: Int = -1,
    @SerializedName("isEnabled")
    var isEnabled: Boolean = false,
    @SerializedName("message")
    var message: String = "",
    @SerializedName("notificationDateTime")
    var notificationDateTime: String = "",
    @SerializedName("notificationID")
    var notificationID: Int = -1,
    @SerializedName("title")
    var title: String = "",
    @SerializedName("type")
    var type: String = ""

)