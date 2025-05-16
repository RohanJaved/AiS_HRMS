package com.appinsnap.aishrm.ui.fragments.employeeattendance.model

import com.google.gson.annotations.SerializedName

data class BodyX(
    @SerializedName("department")
    var department: String = "",
    @SerializedName("departmentId")
    var departmentId: Int = -1,
    @SerializedName("designation")
    var designation: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("employeeID")
    var employeeID: Int = -1,
    @SerializedName("firstCheckIn")
    var firstCheckIn: String = "",
    @SerializedName("lastCheckOut")
    var lastCheckOut: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("profileURL")
    var profileURL: Any = "",
    @SerializedName("remarks")
    var remarks: String = "",
    @SerializedName("reportingTo")
    var reportingTo: String = "",
    @SerializedName("vertical")
    var vertical: String = "",
    @SerializedName("status")
    var status: Boolean = false,
    @SerializedName("value")
    var value: String = ""

)