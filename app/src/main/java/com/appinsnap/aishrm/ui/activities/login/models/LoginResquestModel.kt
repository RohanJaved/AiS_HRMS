package com.appinsnap.aishrm.ui.activities.login.models

import com.google.gson.annotations.SerializedName


data class LoginResquestModel(
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("device_id")
    val device_id: String,
    @SerializedName("device")
    val device:Int
)