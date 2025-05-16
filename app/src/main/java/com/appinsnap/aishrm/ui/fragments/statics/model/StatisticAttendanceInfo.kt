package com.appinsnap.aishrm.ui.fragments.statics.model

import com.appinsnap.aishrm.ui.fragments.home.models.attendancegraph.CtoAndManagerModelresponceSummery
import com.appinsnap.aishrm.ui.fragments.home.models.attendancegraph.EmployeedetailModelresponces
import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate
import java.io.Serializable

data class StatisticAttendanceInfo(
    @SerializedName("from")
    var from: String = "",
    @SerializedName("ctoAndManagerModelresponceSummery")
    val ctoAndManagerModelresponceSummery: List<CtoAndManagerModelresponceSummery>?= null,
    @SerializedName("employeedetailModelresponces")
    val employeedetailModelresponces: List<EmployeedetailModelresponces>?= null,
    @SerializedName("departmentlist")
    val departmentlist:List<Int>,
    @SerializedName("genderlist")
    val genderlist:List<Int>,
    @SerializedName("designationlist")
    val designationlist:List<Int>,
    @SerializedName("officelist")
    val officelist:List<Int>,
    @SerializedName("date")
    var date: LocalDate? = null,
    @SerializedName("grouplist")
    val grouplist:List<Int>
): Serializable
