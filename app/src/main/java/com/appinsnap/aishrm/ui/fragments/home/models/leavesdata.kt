package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class leavesdata(
    @SerializedName("leavestitle")
    var leavestitle:String,
    @SerializedName("totalleaves")
    var totalleaves:String, var icon: Int) {
}