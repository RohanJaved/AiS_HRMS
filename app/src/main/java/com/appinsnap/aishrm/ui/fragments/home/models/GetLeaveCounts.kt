package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class GetLeaveCounts(
    @SerializedName("availableLeaves")
    val availableLeaves: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("id")
    val id:Int
)