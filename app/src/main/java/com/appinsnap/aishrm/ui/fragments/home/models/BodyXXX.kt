package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class BodyXXX(
    @SerializedName("attendanceSummary")
    val attendanceSummary: List<AttendanceSummary>,
    @SerializedName("weeklyAttendanceresponce")
    val weeklyAttendanceresponce: List<WeeklyAttendanceresponce>
)