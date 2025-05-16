package com.appinsnap.aishrm.base

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ErrorResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("response")
    val data: Data,
    @SerializedName("responseCode")
    val statusCode: Int
) {
    @Keep
    data class Data(
        @SerializedName("message")
        val message: String
    )
}