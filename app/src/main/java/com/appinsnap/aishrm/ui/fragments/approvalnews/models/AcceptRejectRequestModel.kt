package com.appinsnap.aishrm.ui.fragments.approvalnews.models

import com.google.gson.annotations.SerializedName

data class AcceptRejectRequestModel(
    @SerializedName("noticationIDs")
    val noticationIDs: String,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("status")
    val status: String
)