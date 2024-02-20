package com.hotta.hoho.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.hotta.hoho.Statics

class FireBaseAuthUtils {
    companion object {

        private lateinit var auth: FirebaseAuth
        private lateinit var database: DatabaseReference
        fun getUid(): String {

            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()
        }

        fun signOut() {
            Statics.UID = ""
            auth.signOut()
        }

    }
}