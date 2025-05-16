package com.appinsnap.aishrm.ui.fragments.home.models.filtermodels

import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("lov")
    val lov: List<Lov>,
    @SerializedName("type")
    val type: String,
    @SerializedName("selectedId")
    var selectedId :Int = 0
)