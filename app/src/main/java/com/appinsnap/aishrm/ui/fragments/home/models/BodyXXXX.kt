package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class BodyXXXX(
    @SerializedName("employeePendingCountresponce")
    val employeePendingCountresponce: EmployeePendingCountresponce,
    @SerializedName("getUserDash")
    val getUserDash: GetUserDash
)