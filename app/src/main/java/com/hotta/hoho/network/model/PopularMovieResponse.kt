package com.hotta.hoho.network.model

import com.google.gson.annotations.SerializedName
import com.hotta.hoho.datamodel.PopularMovieResult

data class PopularMovieResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val movies: Collection<PopularMovieResult>,
    @SerializedName("totla_pages") val pages: Int
)