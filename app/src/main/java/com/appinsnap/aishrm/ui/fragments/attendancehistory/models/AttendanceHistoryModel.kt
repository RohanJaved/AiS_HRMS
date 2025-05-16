package com.appinsnap.aishrm.ui.fragments.attendancehistory.models

import com.google.gson.annotations.SerializedName

data class AttendanceHistoryModel(
    @SerializedName("checkInTimeHistory")
    var checkInTimeHistory: String = "",

    @SerializedName("checkOutTimeHistory")
    var checkOutTimeHistory: String = "",

    @SerializedName("dateHistory")
    var dateHistory: String = "",

    @SerializedName("emailHistory")
    var emailHistory: String = "",

    @SerializedName("nameHistory")
    var nameHistory: String = ""

) {
}