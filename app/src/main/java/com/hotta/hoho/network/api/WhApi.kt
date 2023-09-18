package com.hotta.hoho.network.api

import com.hotta.hoho.datamodel.WeatherResult
import com.hotta.hoho.network.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface WhApi {
    companion object {
        const val API_KEY = "p66r4HRcRz5NoHnddaCQAgA5wrbWc1rTnn3qcMshgFO%2BZRT7FtCYPNghnCAYn6gt3xTe%2BwrEmDMoCrAc5yAtXQ%3D%3D"
    }

    @GET("1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=$API_KEY")
    suspend fun getWeather(
        @Query("dataType") dataType: String,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("base_date") baseDate: Int,
        @Query("base_time") baseTime: Int,
        @Query("nx") nx: String,
        @Query("ny") ny: String
    ): Response<WeatherResult>
}