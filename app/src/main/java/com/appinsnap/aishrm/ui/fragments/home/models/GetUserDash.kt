package com.appinsnap.aishrm.ui.fragments.home.models
import com.google.gson.annotations.SerializedName

data class GetUserDash(
    @SerializedName("annualLeaves")
    val annualLeaves: String,
    @SerializedName("appliedDaysStatus")
    val appliedDaysStatus: Boolean,
    @SerializedName("casualLeaves")
    val casualLeaves: String,
    @SerializedName("designation")
    val designation: String,
    @SerializedName("employeID")
    val employeID: String,
    @SerializedName("employeeLocation")
    val employeeLocation: String,
    @SerializedName("firstCheckIn")
    val firstCheckIn: Any,
    @SerializedName("fromDate")
    val fromDate: String,
    @SerializedName("halfLeaves")
    val halfLeaves: Any,
    @SerializedName("lastActivity")
    val lastActivity: String,
    @SerializedName("lastCheckOut")
    val lastCheckOut: Any,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("marriageLeaves")
    val marriageLeaves: String,
    @SerializedName("medicalLeaves")
    val medicalLeaves: Any,
    @SerializedName("paternityLeaves")
    val paternityLeaves: Any,
    @SerializedName("radius")
    val radius: String,
    @SerializedName("toDate")
    val toDate: String,
    @SerializedName("viewExcess")
    val viewExcess: String
)