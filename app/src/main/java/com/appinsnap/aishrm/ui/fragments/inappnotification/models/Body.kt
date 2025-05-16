package com.appinsnap.aishrm.ui.fragments.inappnotification.models


import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("employeeID")
    val employeeID: Int,
    @SerializedName("isEnabled")
    val isEnabled: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("notificationID")
    val notificationID: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("notificationDateTime")
    var notificationDateTime:String
)