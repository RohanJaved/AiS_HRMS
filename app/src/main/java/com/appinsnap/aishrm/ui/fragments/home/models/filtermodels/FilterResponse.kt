package com.appinsnap.aishrm.ui.fragments.home.models.filtermodels

import com.google.gson.annotations.SerializedName

data class FilterResponse(
    @SerializedName("body")
    val body: List<Body>,
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("statusMessage")
    val statusMessage: String,
    @SerializedName("traceId")
    val traceId: String
)