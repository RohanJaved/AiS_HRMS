package com.appinsnap.aishrm.ui.fragments.home.models

import com.google.gson.annotations.SerializedName

data class SettingApiResponse(
    @SerializedName("body")
    val body: Body?,
    @SerializedName("statusCode")
    val statusCode: String? = "",
    @SerializedName("statusMessage")
    val statusMessage: String? = "",
    @SerializedName("traceId")
    val traceId: String? = ""
) {

    data class Body(
        @SerializedName("attendanceTypes")
        val attendanceTypes: List<AttendanceType?>? = null,
        @SerializedName("dashboardTiles")
        val dashboardTiles: List<DashboardTile?>? = null,
        @SerializedName("locations")
        val locations: List<Location?>? = null,
        @SerializedName("promotionalImages")
        val promotionalImages: List<PromotionalImage?>? = null,
        @SerializedName("categories")
        val categories: List<categories>
    )

    data class PromotionalImage(
        @SerializedName("filePath")
        val filePath: String? = null,
        @SerializedName("id")
        val id: Int? = null
    )

    data class Location(
        @SerializedName("latitude")
        val latitude: Double? = null,
        @SerializedName("longitude")
        val longitude: Double? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("radius")
        val radius: Int? = null
    )

    data class DashboardTile(
        @SerializedName("icon")
        val icon: String? = null,
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("name")
        val name: String? = null
    )

    data class AttendanceType(
        @SerializedName("hasComments")
        val hasComments: Boolean? = null,
        @SerializedName("hasMultipleDays")
        val hasMultipleDays: Boolean? = null,
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("title")
        val title: String? = null,
        var isCheck: Boolean = false
    )
}

data class categories(
    @SerializedName("id")
    val id:Int?=null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("hastime")
    val hastime: Boolean? = null,
    @SerializedName("hasdate")
    val hasdate: Boolean? = null,
    @SerializedName("hasleavetype")
    val hasleavetype: Boolean? = null,
    @SerializedName("hascomment")
    val hascomment: Boolean? = null
)
