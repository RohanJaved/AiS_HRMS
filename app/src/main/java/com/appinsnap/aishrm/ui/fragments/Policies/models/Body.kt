package com.appinsnap.aishrm.ui.fragments.Policies.models

import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("attachmentlist")
    val attachmentlist: Any,
    @SerializedName("getPolicies")
    val getPolicies: Any,
    @SerializedName("getPoliciesTypes")
    val getPoliciesTypes: List<GetPoliciesType>,
    @SerializedName("getPoliciesresponse")
    val getPoliciesresponse: List<GetPoliciesresponse>,
    @SerializedName("getSOPS")
    val getSOPS: Any,
    @SerializedName("getSOPSTypes")
    val getSOPSTypes: List<GetSOPSType>,
    @SerializedName("getSOPresponse")
    val getSOPresponse: List<GetSOPresponse>,
    @SerializedName("sopAttachmentlist")
    val sopAttachmentlist: Any
)