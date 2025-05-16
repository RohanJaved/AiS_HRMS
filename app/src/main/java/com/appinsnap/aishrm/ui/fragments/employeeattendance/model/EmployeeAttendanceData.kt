package com.appinsnap.aishrm.ui.fragments.employeeattendance.model

import com.google.gson.annotations.SerializedName

class EmployeeAttendanceData(
    @SerializedName("name")
    var name:String,
    @SerializedName("status")
    var status:String,
    @SerializedName("checkintime")
    var checkintime:String,
    @SerializedName("checkouttime")
    var checkouttime:String) {
}