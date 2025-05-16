package com.appinsnap.aishrm.ui.fragments.Policies.models

import com.google.gson.annotations.SerializedName

data class GetSOPSType(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)