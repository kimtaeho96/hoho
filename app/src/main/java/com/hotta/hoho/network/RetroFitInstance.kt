package com.hotta.hoho.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetroFitInstance {
    //	test
    private const val TEST_BASE_URL = "http://www.kobis.or.kr"

    private val client = Retrofit.Builder().baseUrl(TEST_BASE_URL).addConverterFactory(
        GsonConverterFactory.create()
    ).build()

    fun getInstance(): Retrofit {
        return client
    }

    //////////////////////////////

    //날씨
    private const val WEATHER_BASE_URL = "http://apis.data.go.kr/"
    private val wh_client = Retrofit.Builder().baseUrl(WEATHER_BASE_URL).addConverterFactory(
        GsonConverterFactory.create()
    ).build()

    fun whGetInstance(): Retrofit {
        return wh_client
    }

    //////////////////////////////

    //영화
    private const val MOVIE_BASE_URL = "https://api.themoviedb.org/3/"

    private val mv_client =
        Retrofit.Builder().baseUrl(MOVIE_BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()

    fun mvGetInstance(): Retrofit {
        return mv_client
    }
    //////////////////////////////

    //카카오맵
    //	a3938b7a7b312662ff65b814874f0079
    private const val KAKAO_BASE_URL = "https://dapi.kakao.com/"

    private val ka_client = Retrofit.Builder().baseUrl(KAKAO_BASE_URL).addConverterFactory(
        GsonConverterFactory.create()
    ).build()

    fun kaGetInstance(): Retrofit {
        return ka_client
    }



}