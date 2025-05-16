package com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model


import com.google.gson.annotations.SerializedName

data class DownloadAttendanceReq(
    @SerializedName("dID")
    var dID: Int? = -1,
    @SerializedName("employeeID")
    var employeeID: String?="",
    @SerializedName("fromDate")
    var fromDate: String?="",
    @SerializedName("toDate")
    var toDate: String?="",
    @SerializedName("isSelf")
    var IsSelf:Boolean=false
)