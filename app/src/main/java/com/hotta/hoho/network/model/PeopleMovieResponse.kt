package com.hotta.hoho.network.model

import com.google.gson.annotations.SerializedName
import com.hotta.hoho.datamodel.BoxOfficeResult
import com.hotta.hoho.datamodel.PeopleMovieResult

data class PeopleMovieResponse(
    @SerializedName("cast")
    var cast: List<PeopleMovieResult>,
    @SerializedName("crew")
    var crew: List<Any>?,
    @SerializedName("id")
    var id: Int

)