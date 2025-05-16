package com.appinsnap.aishrm.ui.fragments.Policies.models

import com.google.gson.annotations.SerializedName

data class SopAttachmentlist(
    @SerializedName("attachments")
    val attachments: String,
    @SerializedName("title")
    val title: String
)