package com.appinsnap.aishrm.ui.fragments.home.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable


class DepartmentGroup(
    @SerializedName("name")
    var name:String
    ):Serializable