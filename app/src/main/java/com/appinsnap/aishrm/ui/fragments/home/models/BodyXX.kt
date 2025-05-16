package com.appinsnap.aishrm.ui.fragments.home.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable


data class BodyXX(
    @SerializedName("absentCount")
    var absentCount: Int,
    @SerializedName("departmentName")
    var departmentName: String,
    @SerializedName("group")
    var group: String,
    @SerializedName("lateCount")
    var lateCount: Int,
    @SerializedName("leaveCount")
    var leaveCount:Int,
    @SerializedName("presentCount")
    var presentCount: Int,
    @SerializedName("departmentId")
    var departmentId:Int,
):Serializable