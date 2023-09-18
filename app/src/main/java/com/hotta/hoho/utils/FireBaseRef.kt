package com.hotta.hoho.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FireBaseRef {
    companion object {
        val database = Firebase.database

        val movieReview = database.getReference("movieReview")
        val userReview = database.getReference("userReview")
        val reviewLike = database.getReference("ReviewLike")
        val userInfo = database.getReference("userInfo")
        val userMsgRef = database.getReference("userMsg")
        val emailCheck = database.getReference("emailCheck")
    }
}