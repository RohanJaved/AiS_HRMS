package com.appinsnap.aishrm.ui.fragments.Policies.models

import com.google.gson.annotations.SerializedName

data class GetPoliciesresponse(
    @SerializedName("attachmentlist")
    val attachmentlist: ArrayList<Attachmentlist>,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: Int
)