package com.appinsnap.aishrm.ui.fragments.latesitting.models

import com.google.gson.annotations.SerializedName

data class Project(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)