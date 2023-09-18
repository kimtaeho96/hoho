package com.hotta.hoho.network.model

import com.google.gson.annotations.SerializedName
import com.hotta.hoho.datamodel.ViewMovieResult

data class VideoMovieResponse(
    var id: String,
    @SerializedName("results")
    var result: List<ViewMovieResult>


)