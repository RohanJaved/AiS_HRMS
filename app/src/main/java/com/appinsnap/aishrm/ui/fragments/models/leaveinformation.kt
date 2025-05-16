package com.appinsnap.aishrm.ui.fragments.models

import com.google.gson.annotations.SerializedName

data class leaveinformation(
    @SerializedName("leavetitle")
    var leavetitle:String,
    @SerializedName("date")
    var date:String,
    @SerializedName("leavestatus")
    var leavestatus:String,
    @SerializedName("datetitle")
    var datetitle:String)