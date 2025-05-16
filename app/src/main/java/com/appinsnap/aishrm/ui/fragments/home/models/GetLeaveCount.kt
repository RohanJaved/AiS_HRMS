package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class GetLeaveCount(
    @SerializedName("availLeave")
    val availLeave: Int,
    @SerializedName("availableLeaves")
    val availableLeaves: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("totalLeave")
    val totalLeave: Int
)