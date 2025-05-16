package com.appinsnap.aishrm.ui.fragments.Policies.models

import com.google.gson.annotations.SerializedName

data class PoliciesSOPResponse(
    @SerializedName("body")
    val body: Body,
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("statusMessage")
    val statusMessage: String,
    @SerializedName("traceId")
    val traceId: String
)