package com.appinsnap.aishrm.ui.activities.mainactivity.models

import com.google.gson.annotations.SerializedName

data class NotificationCountRequestModel(
    @SerializedName("email")
    var email: String
)