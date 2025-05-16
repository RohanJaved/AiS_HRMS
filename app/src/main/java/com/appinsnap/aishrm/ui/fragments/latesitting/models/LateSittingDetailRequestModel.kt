package com.appinsnap.aishrm.ui.fragments.latesitting.models

import com.google.gson.annotations.SerializedName

data class LateSittingDetailRequestModel(
    @SerializedName("employeeid")
    val employeeid: Int,
    @SerializedName("notiid")
    val notiid: Int
)