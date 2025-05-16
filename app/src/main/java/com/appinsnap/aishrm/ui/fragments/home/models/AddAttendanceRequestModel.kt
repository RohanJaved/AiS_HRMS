package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName


data class AddAttendanceRequestModel(
    @SerializedName("employeeID")
    var employeeID: String,
    @SerializedName("latitude")
    var latitude: Double,
    @SerializedName("longitude")
    var longitude: Double,
    @SerializedName("status")
    var status: String,
    @SerializedName("comment")
    var comment:String ="",
    @SerializedName("employeeLocation")
    var employeeLocation:String,
    @SerializedName("noofDays")
    var noofDays:String,
    @SerializedName("fromDate")
    var fromDate:String,
    @SerializedName("toDate")
    var toDate:String,
    @SerializedName("selectedcheckouttime")
    var selectedcheckouttime:String="",
    @SerializedName("isforgotcheckout")
    var isforgotcheckout:Boolean
)