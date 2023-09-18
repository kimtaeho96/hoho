package com.hotta.hoho.network.model

import com.google.gson.annotations.SerializedName
import com.hotta.hoho.datamodel.BoxOfficeResult

data class MovieResponse(

    @SerializedName("boxOfficeResult")
    var boxofficeResult: BoxOfficeResult?

)