package com.appinsnap.aishrm.ui.fragments.statics.model

import com.google.gson.annotations.SerializedName

data class DownloadGraphDataRequest(
    @SerializedName("departmentid")
    var departmentid: List<Int?>?,
    @SerializedName("designationid")
    var designationid: List<Int?>?,
    @SerializedName("endDate")
    var endDate: String?,
    @SerializedName("employeeid")
    var employeeid: String?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("genderid")
    var genderid: List<Int?>?,
    @SerializedName("groupid")
    var groupid: List<Int?>?,
    @SerializedName("officeid")
    var officeid: List<Int?>?,
    @SerializedName("startDate")
    var startDate: String?
)
