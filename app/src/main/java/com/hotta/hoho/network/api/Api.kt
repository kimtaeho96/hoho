package com.hotta.hoho.network.api

import com.hotta.hoho.network.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {


    //http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=f5eef3421c602c6cb7ea224104795888&targetDt=20120101
    @GET("/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json")
    suspend fun getDayMovieChart(
        @Query("targetDt") targetDt: String?,
        @Query("key") key: String?
    ): MovieResponse

    @GET("movie/popular")
    suspend fun getPopularMovie(
        @Query("page") page: Int,
        @Query("api_key") key: String = "8f20c3de95e081c58a1a1ca38e4f7d73",
        @Query("language") language: String = "ko"
    ): PopularMovieResponse

    @GET("movie/now_playing")
    suspend fun getCurrentMovie(
        @Query("page") page: Int,
        @Query("api_key") key: String = "8f20c3de95e081c58a1a1ca38e4f7d73",
        @Query("language") language: String = "ko"
    ): PopularMovieResponse

    @GET("movie/top_rated")
    suspend fun getTopMovie(
        @Query("page") page: Int,
        @Query("api_key") key: String = "8f20c3de95e081c58a1a1ca38e4f7d73",
        @Query("language") language: String = "ko"
    ): PopularMovieResponse

    @GET("movie/upcoming")
    suspend fun getUpMovie(
        @Query("page") page: Int,
        @Query("api_key") key: String = "8f20c3de95e081c58a1a1ca38e4f7d73",
        @Query("language") language: String = "ko"

    ): PopularMovieResponse


    @GET("movie/{movie_id}")
    suspend fun getDetailMovie(
        @Path("movie_id") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String = "ko"

    ): DetailMovieResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getVideoMovie(
        @Path("movie_id") id: Int,
        @Query("api_key") key: String = "8f20c3de95e081c58a1a1ca38e4f7d73",

        ): VideoMovieResponse

    @GET(" movie/{movie_id}/credits")
    suspend fun getCreditsMovie(
        @Path("movie_id") id: Int,
        @Query("api_key") key: String = "8f20c3de95e081c58a1a1ca38e4f7d73",

        ): CreditsMovieResponse

    @GET("tv/popular")
    suspend fun getPopularTv(
        @Query("api_key") key: String = "8f20c3de95e081c58a1a1ca38e4f7d73",
        @Query("language") language: String = "ko"
    ): PopularTvResponse

    // 특정 장르의 영화 가져오기
    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("api_key") key: String,
        @Query("with_genres") genreId: String,
        @Query("language") language: String = "ko",

    ): PopularMovieResponse


}