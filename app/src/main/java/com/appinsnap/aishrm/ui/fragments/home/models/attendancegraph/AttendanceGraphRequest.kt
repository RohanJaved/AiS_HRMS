package com.appinsnap.aishrm.ui.fragments.home.models.attendancegraph


import com.google.gson.annotations.SerializedName

data class AttendanceGraphRequest(
    @SerializedName("departmentid")
    var departmentid: List<Int?>?,
    @SerializedName("designationid")
    var designationid: List<Int?>?,
    @SerializedName("endDate")
    var endDate: String?,
    @SerializedName("genderid")
    var genderid: List<Int?>?,
    @SerializedName("groupid")
    var groupid: List<Int?>?,
    @SerializedName("officeid")
    var officeid: List<Int?>?,
    @SerializedName("startDate")
    var startDate: String?
)