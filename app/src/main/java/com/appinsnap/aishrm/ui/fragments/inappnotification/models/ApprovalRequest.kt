package com.appinsnap.aishrm.ui.fragments.inappnotification.models


import com.google.gson.annotations.SerializedName

data class ApprovalRequest(
    @SerializedName("comment")
    val comment: String,
    @SerializedName("employeeID")
    val employeeID: Int,
    @SerializedName("notificationID")
    val notificationID: Int,
    @SerializedName("status")
    val status: String
)