package com.appinsnap.aishrm.ui.fragments.Policies.models

import com.google.gson.annotations.SerializedName

data class GetSOPresponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("sopAttachmentlist")
    val sopAttachmentlist: ArrayList<SopAttachmentlist>,
    @SerializedName("type")
    val type: Int
)