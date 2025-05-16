package com.appinsnap.aishrm.ui.fragments.leaves.models

import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("getLeaveCount")
    val getLeaveCount: List<GetLeaveCount>,
    @SerializedName("recentLeave")
    val recentLeave: List<RecentLeave>
)