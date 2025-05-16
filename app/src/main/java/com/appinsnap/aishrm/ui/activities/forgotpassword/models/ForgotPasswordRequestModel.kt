package com.appinsnap.aishrm.ui.activities.forgotpassword.models

import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequestModel(
    @SerializedName("username")
    val username: String=""
)