package com.hotta.hoho.repository

import com.hotta.hoho.network.api.Api
import com.hotta.hoho.network.api.KakaoApi
import com.hotta.hoho.network.RetroFitInstance
import com.hotta.hoho.network.api.WhApi


class NetworkRepository {
    private val client1 = RetroFitInstance.getInstance().create(Api::class.java)
    private val client2 = RetroFitInstance.mvGetInstance().create(Api::class.java)
    private val kakaoClient = RetroFitInstance.kaGetInstance().create(KakaoApi::class.java)
    private val whClient = RetroFitInstance.whGetInstance().create(WhApi::class.java)

    suspend fun getCurrentMovieList(targetDt: String, key: String) =
        client1.getDayMovieChart(targetDt, key)

    suspend fun getPopularMovie(page: Int) = client2.getPopularMovie(page)
    suspend fun getCurrentMovie(page: Int) = client2.getCurrentMovie(page)
    suspend fun getTopMovie(page: Int) = client2.getTopMovie(page)

    // suspend fun getUpMovie(page: Int) = client2.getUpMovie(page)
    suspend fun getUpMovie(page: Int) = client2.getUpMovie(page)


    suspend fun getDeltailMovie(id: Int, key: String) = client2.getDetailMovie(id, key)

    suspend fun getViedoMovie(id: Int) = client2.getVideoMovie(id)

    suspend fun getCreditsMovie(id: Int) = client2.getCreditsMovie(id)

    suspend fun getPopularTv(key: String) = client2.getPopularTv(key)
    suspend fun getMoviesByGenre(key: String, genre: String) = client2.getMoviesByGenre(key, genre)

    suspend fun getPeopleDetail(id: Int) = client2.getPeopleDetail(id)
    suspend fun getPeopleMovie(id: Int) = client2.getPeopleMovie(id)
    //kakao api
    suspend fun getKakaoMapSearch(key: String, query: String, x: String, y: String, radius: Int) =
        kakaoClient.getSearchKeyword(key, query, x, y, radius)

    suspend fun getWeather(
        dataType: String, numOfRows: Int, pageNo: Int,
        baseDate: Int, baseTime: Int, nx: String, ny: String
    ) = whClient.getWeather(dataType, numOfRows, pageNo, baseDate, baseTime, nx, ny)


}