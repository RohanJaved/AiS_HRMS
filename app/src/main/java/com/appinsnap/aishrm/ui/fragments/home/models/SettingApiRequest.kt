package com.appinsnap.aishrm.ui.fragments.home.models


import com.google.gson.annotations.SerializedName

data class SettingApiRequest(
    @SerializedName("appVersion")
    var appVersion: String ="",
    @SerializedName("deviceModel")
    var deviceModel: String ="",
    @SerializedName("device_name")
    var deviceName: String ="",
    @SerializedName("device_token")
    var deviceToken: String ="",
    @SerializedName("device_version")
    var deviceVersion: String =""
)