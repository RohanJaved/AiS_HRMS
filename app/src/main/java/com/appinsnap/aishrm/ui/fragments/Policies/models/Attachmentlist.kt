package com.appinsnap.aishrm.ui.fragments.Policies.models

import com.google.gson.annotations.SerializedName

data class Attachmentlist(
    @SerializedName("attachments")
    val attachments: String,
    @SerializedName("title")
    val title: String
)