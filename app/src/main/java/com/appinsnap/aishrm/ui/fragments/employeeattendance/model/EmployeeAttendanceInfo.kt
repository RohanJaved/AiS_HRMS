package com.appinsnap.aishrm.ui.fragments.employeeattendance.model

import com.appinsnap.aishrm.ui.fragments.home.models.BodyXX
import com.appinsnap.aishrm.ui.fragments.home.models.DepartmentGroup
import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate
import java.io.Serializable
data class EmployeeAttendanceInfo(
    @SerializedName("employeeID")
    var employeeID: Int? = 0,
    @SerializedName("firstCheckIn")
    var firstCheckIn: String? = "",
    @SerializedName("lastCheckOut")
    var lastCheckOut: String? = "",
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("remarks")
    var remarks: String? = "",
    @SerializedName("designation")
    var designation: String? = "",
    @SerializedName("depID")
    var depID: Int? = 0,
    @SerializedName("status")
    var status: String = "",
    @SerializedName("department")
    var department: String? = "",
    @SerializedName("from")
    var from: String = "",
    @SerializedName("date")
    var date: LocalDate? = null,
    @SerializedName("verticalsList")
    var verticalsList:List<DepartmentGroup>? = null,
    @SerializedName("alldeptlist")
    var alldeptlist:List<BodyXX>?= null
):Serializable