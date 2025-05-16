package com.appinsnap.aishrm.ui.fragments.employeeattendance.model

import com.google.gson.annotations.SerializedName

data class ManagerCtoEmployeeResponse(
    @SerializedName("body")
    var body: List<BodyX> = listOf(),

    @SerializedName("statusCode")
    var statusCode: String = "",

    @SerializedName("statusMessage")
    var statusMessage: String = "",

    @SerializedName("traceId")
    var traceId: String = ""

)