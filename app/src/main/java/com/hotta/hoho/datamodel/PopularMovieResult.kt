package com.hotta.hoho.datamodel

import com.google.gson.annotations.SerializedName

data class PopularMovieResult (
    @SerializedName("id") val id : Long,
    @SerializedName("title") val title : String,
    @SerializedName("overview") val overview : String,
    @SerializedName("poster_path") val poster_path: String
)
