package com.hotta.hoho.network.model

import com.google.gson.annotations.SerializedName
import com.hotta.hoho.datamodel.PopularMovieResult
import com.hotta.hoho.datamodel.PopularTvResult

data class PopularTvResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<PopularTvResult>,
    @SerializedName("totla_pages") val pages: Int
)