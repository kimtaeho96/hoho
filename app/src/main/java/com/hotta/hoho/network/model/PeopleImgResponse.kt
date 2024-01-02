package com.hotta.hoho.network.model

import com.google.gson.annotations.SerializedName
import com.hotta.hoho.datamodel.BoxOfficeResult
import com.hotta.hoho.datamodel.PeopleImgResult
import com.hotta.hoho.datamodel.PeopleMovieResult

data class PeopleImgResponse(
    @SerializedName("id")
    var id: Int,
    @SerializedName("profiles")
    var profiles: List<PeopleImgResult>?,


    )