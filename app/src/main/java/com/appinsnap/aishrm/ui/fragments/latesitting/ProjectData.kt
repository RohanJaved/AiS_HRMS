package com.appinsnap.aishrm.ui.fragments.latesitting

import com.google.gson.annotations.SerializedName

class ProjectData(
    @SerializedName("name")
    var name:String,
    @SerializedName("id")
    var id:Int,
    @SerializedName("check")
    var check:Boolean=false,
    @SerializedName("selectedId")
    val selectedId:Int) {

}
