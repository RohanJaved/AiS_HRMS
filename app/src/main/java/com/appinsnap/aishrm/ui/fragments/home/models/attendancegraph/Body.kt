package com.appinsnap.aishrm.ui.fragments.home.models.attendancegraph


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Body(
    @SerializedName("ctoAndManagerModelresponceSummery")
    val ctoAndManagerModelresponceSummery: List<CtoAndManagerModelresponceSummery> = emptyList(),
    @SerializedName("employeedetailModelresponces")
    val employeedetailModelresponces: List<EmployeedetailModelresponces> = emptyList()
)

data class CtoAndManagerModelresponceSummery(

    @SerializedName("absentCount")
    var absentCount: Int?=null,
    @SerializedName("attendanceDate")
    var attendanceDate: String?=,
    @SerializedName("lateCount")
    var lateCount: Int?,
    @SerializedName("onLeaveCount")
    var onLeaveCount: Int?,
    @SerializedName("presentCount")
    var presentCount: Int?,
    @SerializedName("presentColor")
    var presentColor: String?,
    @SerializedName("absentColor")
    var absentColor: String?,
    @SerializedName("leaveColor")
    var leaveColor: String?,
    @SerializedName("lateColor")
    var lateColor: String?,
    @SerializedName("totalEmp")
    var totalEmp: Int? = null,
    var pIndex: Int = 100,
    var aIndex: Int = 100,
    var leaIndex: Int = 100
): Serializable

data class EmployeedetailModelresponces(

    @SerializedName("employeeID")
    var employeeID: Int?=null,
    @SerializedName("remarks")
    var remarks: String?=null,
    @SerializedName("name")
    var name: String?=null,
    @SerializedName("designation")
    var designation: String?=null,
    @SerializedName("profileURL")
    var profileURL: String?=null,
    @SerializedName("reportingTo")
    var reportingTo: String?=null,
    @SerializedName("department")
    var department: String?=null,
    @SerializedName("vertical")
    var vertical: String?=null,
    @SerializedName("email")
    var email: String?=null,
    @SerializedName("departmentId")
    var departmentId:Int?=null,
    @SerializedName("firstCheckIn" )
    var firstCheckIn : String?=null,
    @SerializedName("lastCheckOut" )
    var lastCheckOut : String?=null
): Serializable