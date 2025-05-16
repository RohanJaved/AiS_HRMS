package com.appinsnap.aishrm.ui.fragments.latesitting.models

import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("checkin")
    val checkin: Any,
    @SerializedName("checkout")
    val checkout: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("firstCheckIn")
    val firstCheckIn: String,
    @SerializedName("project")
    val project: String,
    @SerializedName("projects")
    val projects: List<Project>,
    @SerializedName("totalhrs")
    val totalhrs: String
)