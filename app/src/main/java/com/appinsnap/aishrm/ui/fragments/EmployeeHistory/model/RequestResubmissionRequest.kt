package com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model

import com.google.gson.annotations.SerializedName

data class RequestResubmissionRequest(
    @SerializedName("category")
    val category: Int,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("employeeID")
    val employeeID: Int,
    @SerializedName("fromDate")
    val fromDate: String,
    @SerializedName("leavetype")
    val leavetype: Int,
    @SerializedName("time")
    val time: String,
    @SerializedName("time2")
    val time2: String,
    @SerializedName("toDate")
    val toDate: String
)