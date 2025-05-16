package com.appinsnap.aishrm.ui.fragments.inappnotification.models

import com.google.gson.annotations.SerializedName

data class NotificationViewRequest(
    @SerializedName("email")
    val email: String
)