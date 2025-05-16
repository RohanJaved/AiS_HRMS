package com.appinsnap.aishrm.ui.fragments.approvalnews.models

import com.google.gson.annotations.SerializedName

data class GetNotificationRequest(
    @SerializedName("email")
    val email: String
)