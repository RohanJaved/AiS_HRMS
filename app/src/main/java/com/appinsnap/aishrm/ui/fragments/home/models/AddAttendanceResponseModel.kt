package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName


data class AddAttendanceResponseModel(
    @SerializedName("statusCode"    ) var statusCode    : String? = null,
    @SerializedName("statusMessage" ) var statusMessage : String? = null,
    @SerializedName("traceId"       ) var traceId       : String? = null,
    @SerializedName("body"          ) var body          : String? = null
)