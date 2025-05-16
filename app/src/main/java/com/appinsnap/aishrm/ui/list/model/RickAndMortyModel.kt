package com.appinsnap.aishrm.ui.list.model

import com.google.gson.annotations.SerializedName

data class RickAndMortyModel(
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val results: List<Result>
)