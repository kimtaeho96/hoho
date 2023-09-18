package com.hotta.hoho.datamodel

import com.google.gson.annotations.SerializedName

data class BoxOfficeResult(
    @SerializedName("boxofficeType")
    val boxofficeType: String,
    @SerializedName("showRange")
    val showRange: String,
    @SerializedName("dailyBoxOfficeList")
    var dailyBoxOfficeList: List<MovieDto>? = null
)

data class MovieDto(
    @SerializedName("movieNm")
    var movieNm: String?,
    @SerializedName("rankInten")
    var rankInten: String?,
    @SerializedName("rank")
    var rank: String?
    )