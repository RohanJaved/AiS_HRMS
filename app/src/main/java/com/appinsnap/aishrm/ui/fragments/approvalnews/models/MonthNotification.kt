package com.appinsnap.aishrm.ui.fragments.approvalnews.models

import com.google.gson.annotations.SerializedName

data class MonthNotification(
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
    var notificationID: Int = -1,

    @SerializedName("title")
    var title: String = "",

    @SerializedName("toDate")
    var toDate: String = "",

    @SerializedName("type")
    var type: String = "",

    @SerializedName("attachment")
    var attachment: String = "",

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