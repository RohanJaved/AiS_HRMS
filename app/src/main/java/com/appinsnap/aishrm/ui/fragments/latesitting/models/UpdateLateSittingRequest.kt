package com.appinsnap.aishrm.ui.fragments.latesitting.models

import com.google.gson.annotations.SerializedName

data class UpdateLateSittingRequest(
    @SerializedName("checkouttime")
    val checkouttime: String,
    @SerializedName("notiid")
    val notiid: Int,
    @SerializedName("totalhrs")
    val totalhrs: String
)