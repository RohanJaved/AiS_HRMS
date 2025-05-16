package com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model

import com.google.gson.annotations.SerializedName

data class RequestResubmissionResponse(
    @SerializedName("body")
    var body: Any = "",
    @SerializedName("statusCode")
    var statusCode: String = "",
    @SerializedName("statusMessage")
    var statusMessage: String = "",
    @SerializedName("traceId")
    var traceId: String = ""

)