package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName


data class Body(
    @SerializedName("lastActivity")
    val lastActivity: String,
    @SerializedName("annualLeaves")
    val annualLeaves: String,
    @SerializedName("casualLeaves")
    val casualLeaves: String,
    @SerializedName("halfLeaves")
    val halfLeaves: String,
    @SerializedName("firstCheckIn")
    val firstCheckIn: String,
    @SerializedName("lastCheckOut")
    val lastCheckOut: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("marriageLeaves")
    val marriageLeaves: String,
    @SerializedName("medicalLeaves")
    val medicalLeaves: String,
    @SerializedName("paternityLeaves")
    val paternityLeaves: String,
    @SerializedName("radius")
    val radius: String,
    @SerializedName("appliedDaysStatus")
    val appliedDaysStatus: Boolean,
    @SerializedName("employeeLocation")
    var employeeLocation:String,
    @SerializedName("fromDate")
    var fromDate:String,
    @SerializedName("toDate")
    var toDate:String,
    @SerializedName("viewExcess")
    var viewExcess:String

)