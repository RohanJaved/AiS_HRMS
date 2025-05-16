package com.appinsnap.aishrm.ui.fragments.leaves.models

import com.google.gson.annotations.SerializedName

data class LeavesDetailRequestModel(
    @SerializedName("employeeID")
    val employeeID: Int
)