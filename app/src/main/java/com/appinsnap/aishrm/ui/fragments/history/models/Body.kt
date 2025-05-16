package com.appinsnap.aishrm.ui.fragments.history.models

import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("employeeID")
    var employeeID: Int = -1,
    @SerializedName("fromDate")
    var fromDate: String = "",
    @SerializedName("isEnabled")
    var isEnabled: Boolean = false,
    @SerializedName("message")
    var message: String = "",
    @SerializedName("notificationDateTime")
    var notificationDateTime: String = "",
    @SerializedName("notificationID")
    var notificationID: Int = 0,
    @SerializedName("status")
    var status: Any? = null,
    @SerializedName("title")
    var title: String = "",
    @SerializedName("toDate")
    var toDate: String = "",
    @SerializedName("type")
    var type: String = "name",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("checkin")
    var checkin: String = "",
    @SerializedName("checkout")
    var checkout: String = "",
    @SerializedName("category")
    var category: String = "",
    @SerializedName("leaveType")
    var leaveType: String = "",
    @SerializedName("comments")
    var comments: String = "",
    @SerializedName("typeid")
    var typeid: Int = -1

)