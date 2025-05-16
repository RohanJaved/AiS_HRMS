package com.appinsnap.aishrm.ui.fragments.LeaaveApplication.models

import com.google.gson.annotations.SerializedName
import java.io.File

data class LeaveRequestModel(
    @SerializedName("attachment")
    val attachment: List<File>?,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("employeeID")
    val employeeID: Int,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("leaveTypeID")
    val leaveTypeID: Int,
    @SerializedName("noofDays")
    val noofDays: String,
    @SerializedName("startDate")
    val startDate: String
)