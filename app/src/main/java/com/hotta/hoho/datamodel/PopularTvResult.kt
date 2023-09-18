package com.hotta.hoho.datamodel

import com.google.gson.annotations.SerializedName

data class PopularTvResult (
    @SerializedName("id") val id : Long,
    @SerializedName("name") val name : String,
    @SerializedName("poster_path") val poster_path: String
)
