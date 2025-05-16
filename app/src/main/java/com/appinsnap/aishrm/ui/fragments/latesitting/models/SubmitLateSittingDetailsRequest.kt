package com.appinsnap.aishrm.ui.fragments.latesitting.models

import com.google.gson.annotations.SerializedName

data class SubmitLateSittingDetailsRequest(
    @SerializedName("checkoutime")
    val checkoutime: String,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("employeeID")
    val employeeID: String,
    @SerializedName("employeeLocation")
    val employeeLocation: String,
    @SerializedName("fromDate")
    val fromDate: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("noofDays")
    val noofDays: String,
    @SerializedName("project")
    val project: List<Int>,
    @SerializedName("status")
    val status: String,
    @SerializedName("toDate")
    val toDate: String,
    @SerializedName("totalHrs")
    val totalHrs: String
)