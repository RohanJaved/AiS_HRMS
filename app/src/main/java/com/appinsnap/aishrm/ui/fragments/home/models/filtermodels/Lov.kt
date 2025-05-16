package com.appinsnap.aishrm.ui.fragments.home.models.filtermodels

import com.google.gson.annotations.SerializedName

data class Lov(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("check")
    var check : Boolean= false
)