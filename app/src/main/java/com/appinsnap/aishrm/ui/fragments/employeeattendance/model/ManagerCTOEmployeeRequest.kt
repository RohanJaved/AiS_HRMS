package com.appinsnap.aishrm.ui.fragments.employeeattendance.model

import com.google.gson.annotations.SerializedName

data class ManagerCTOEmployeeRequest(
    @SerializedName("currentDate")
    val currentDate: String,
    @SerializedName("dID")
    val dID: Int,
    @SerializedName("employeeID")
    val employeeID: Int
)