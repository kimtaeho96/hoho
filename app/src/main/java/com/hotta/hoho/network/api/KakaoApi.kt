package com.hotta.hoho.network.api

import com.hotta.hoho.network.model.*
import com.hotta.hoho.view.map.MapDataModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoApi {


    //http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=f5eef3421c602c6cb7ea224104795888&targetDt=20120101
    @GET("v2/local/search/keyword.json") // Keyword.json의 정보를 받아옴
    suspend fun getSearchKeyword(
        @Header("Authorization") key: String, // 카카오 API 인증키 [필수]
        @Query("query") query: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("radius") radius: Int
    ): MapDataModel


}