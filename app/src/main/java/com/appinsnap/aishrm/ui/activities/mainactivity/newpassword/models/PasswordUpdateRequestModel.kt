package com.appinsnap.aishrm.ui.activities.mainactivity.newpassword.models

import com.google.gson.annotations.SerializedName

data class PasswordUpdateRequestModel(
    @SerializedName("newPassword")
    val newPassword: String="",
    @SerializedName("userID")
    val userID: String=""
)