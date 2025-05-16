package com.appinsnap.aishrm.util

import android.webkit.MimeTypeMap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun File.getPart(api_key_name: String): MultipartBody.Part? {
    return try {
        MultipartBody.Part.createFormData(
            name = api_key_name,
            filename = this.name ?: "Attachment",
            body = this.asRequestBody(
                MimeTypeMap
                    .getSingleton()
                    .getMimeTypeFromExtension(this.extension)
                    ?.toMediaType()
            )
        )
    } catch (e: Exception) {
       e.printStackTrace()
        null
    }
}