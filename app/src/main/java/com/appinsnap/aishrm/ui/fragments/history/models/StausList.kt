package com.appinsnap.aishrm.ui.fragments.history.models

import com.google.gson.annotations.SerializedName

data class StausList(
    @SerializedName("notificationtitle")
    var notificationtitle: String = "",
    @SerializedName("notificationmessage")
var notificationmessage: String = "")
{
}