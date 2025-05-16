package com.appinsnap.aishrm.ui.activities.otp.models

import com.google.gson.annotations.SerializedName

data class OTPRequestModel(
    @SerializedName("email")
    val email: String,
    @SerializedName("otpCode")
    val otpCode: String
)