package com.appinsnap.aishrm.ui.activities.login.models


import com.google.gson.annotations.SerializedName


data class BodyX(
    @SerializedName("cellNumber")
    var cellNumber: String="",
    @SerializedName("department")
    var department: String="",
    @SerializedName("designation")
    var designation: String="",
    @SerializedName("emergencyNumber")
    var emergencyNumber: String="",
    @SerializedName("employeeID")
    var employeeID: String="",
    @SerializedName("fullName")
    var fullName: String="",
    @SerializedName("profileURL")
    var profileURL: String="",
    @SerializedName("reportingTo")
    var reportingTo: String="",
    @SerializedName("email")
    var email: String="",
    @SerializedName("appVersion")
    var appVersion:String="",
    @SerializedName("viewTypeID")
    var viewTypeID:Int=0,
    @SerializedName("memberCount")
    var memberCount:Int=0,
    @SerializedName("departmentID")
    var departmentID:Int=0,
    @SerializedName("isPMObit")
    var isPMObit:Boolean=false
)