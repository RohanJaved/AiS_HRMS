package com.appinsnap.aishrm.ui.fragments.home.models.statisticsmodels


import com.google.gson.annotations.SerializedName

data class GetStatisticsGraphRequest(
    @SerializedName("currentDate")
    val currentDate: String,
    @SerializedName("departmentid")
    val departmentid: Int,
    @SerializedName("designationid")
    val designationid: Int,
    @SerializedName("employeeID")
    val employeeID: Int,
    @SerializedName("genderid")
    val genderid: Int,
    @SerializedName("groupid")
    val groupid: Int,
    @SerializedName("officeid")
    val officeid: Int
)