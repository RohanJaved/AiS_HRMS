package com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model

import com.appinsnap.aishrm.ui.fragments.employeeattendance.model.EmployeeAttendanceInfo
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RequestAttendanceInformation(
    @SerializedName("attendancestatus")
    var attendancestatus: String = "",
     @SerializedName("date")
     var date: String = "",
     @SerializedName("empdetails")
     var empdetails:EmployeeAttendanceInfo?=null
):Serializable {
}