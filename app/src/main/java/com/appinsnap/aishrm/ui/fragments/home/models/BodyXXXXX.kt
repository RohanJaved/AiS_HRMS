package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class BodyXXXXX(
    @SerializedName("getLeaveCounts")
    val getLeaveCounts: List<GetLeaveCount>,
    @SerializedName("recentLeaves")
    val recentLeaves: List<RecentLeave>
)