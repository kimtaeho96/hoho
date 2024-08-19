package com.hotta.hoho.view.myreview

data class MyReviewModel(
    val movieName: String = "",
    val posterPath: String = "",
    var moveId: String = "",
    val time: String = "",
    val name: String = "",
    val userid: String = "",
    val goodBad: String = "",
    val text: String = "",
    val count: Int = 0,
)
