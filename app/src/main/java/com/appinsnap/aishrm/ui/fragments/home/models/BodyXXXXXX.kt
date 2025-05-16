package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class BodyXXXXXX(
    @SerializedName("employeePendingCountresponce")
    val employeePendingCountresponce: EmployeePendingCountresponceX,
    @SerializedName("getLeaveCountss")
    val getLeaveCountss: List<GetLeaveCounts>,
    @SerializedName("getUserDash")
    val getUserDash: GetUserDashX,
    @SerializedName("countreasonModel")
    val countreasonModel:CountReasonModel,
    @SerializedName("employeetotalhrs")
    val employeetotalhrs:EmployeeTotalHrs
)