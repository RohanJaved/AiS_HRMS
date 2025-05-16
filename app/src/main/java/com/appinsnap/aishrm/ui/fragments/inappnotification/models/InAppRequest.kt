package com.appinsnap.aishrm.ui.fragments.inappnotification.models


import com.google.gson.annotations.SerializedName

data class InAppRequest(
    @SerializedName("email")
    val managerEmail: String
)