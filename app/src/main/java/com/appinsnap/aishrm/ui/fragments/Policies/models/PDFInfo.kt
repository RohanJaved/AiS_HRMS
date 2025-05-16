package com.appinsnap.aishrm.ui.fragments.Policies.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class PDFInfo(
    var pdfurl:String,var pdfName:String):Serializable {
}