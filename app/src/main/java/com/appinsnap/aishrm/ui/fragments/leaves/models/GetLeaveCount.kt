package com.appinsnap.aishrm.ui.fragments.leaves.models

import com.google.gson.annotations.SerializedName

data class GetLeaveCount(
    @SerializedName("availLeave")
    val availLeave: Int,
    @SerializedName("hasAttachment")
    val hasAttachment: Boolean,
    @SerializedName("hasComments")
    val hasComments: Boolean,
    @SerializedName("hasMultipleDays")
    val hasMultipleDays: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("totalLeave")
    val totalLeave: Int,
    @SerializedName("hasOneDayAttachment")
    var hasOneDayAttachment:Boolean=false,
    @SerializedName("hasTwoDaysAttachment")
    var hasTwoDaysAttachment:Boolean=false,
    @SerializedName("documentcount")
    var documentcount:Int=0
)