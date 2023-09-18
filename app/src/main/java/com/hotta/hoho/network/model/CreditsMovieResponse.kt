package com.hotta.hoho.network.model

import com.google.gson.annotations.SerializedName
import com.hotta.hoho.datamodel.CreditsMovieResult

data class CreditsMovieResponse(
    val id: String,
    @SerializedName("cast")
    val cast: List<CreditsMovieResult>
)