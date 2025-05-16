package com.appinsnap.aishrm.ui.fragments.leavesDetails.models

import java.io.Serializable

data class TodayModel(
    val message: String?,
    val notificationID: Int?,
    val attachment: String?
): Serializable
