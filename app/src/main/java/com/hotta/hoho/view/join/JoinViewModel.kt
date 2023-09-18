package com.hotta.hoho.view.join

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.hotta.hoho.repository.FireBaseRepository
import com.hotta.hoho.utils.FireBaseAuthUtils
import com.hotta.hoho.utils.FireBaseRef
import com.hotta.hoho.view.detail.ReviewModel
import com.hotta.hoho.view.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JoinViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val fireBaseRepository = FireBaseRepository()
    lateinit var emailList: ArrayList<String>

    private val _joinResult = MutableLiveData<Boolean>()
    val joinResult: LiveData<Boolean>
        get() = _joinResult

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean>
        get() = _loginResult

    private val _emailCheckResult = MutableLiveData<List<String>>()
    val emailCheckResult: LiveData<List<String>>
        get() = _emailCheckResult


    fun join(activity: Activity, email: String, password: String) = viewModelScope.launch {
        try {
            Log.d("JoinViewModel1", email.toString())
            Log.d("JoinViewModel1", password.toString())
            auth = Firebase.auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->

                    Log.d("JoinViewModel1", password.toString())
                    _joinResult.value = task.isSuccessful
                }
        } catch (e: Exception) {
            Log.d("JoinViewModel3", e.toString())
        }


    }

    fun login(activity: Activity, email: String, password: String) = viewModelScope.launch {
        try {
            auth = Firebase.auth
            Log.d("JoinViewModel1", email.toString())
            Log.d("JoinViewModel1", password.toString())
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    Log.d("JoinViewModel1", password.toString())
                    _loginResult.value = task.isSuccessful
                }
        } catch (e: Exception) {
            Log.d("JoinViewModel3", e.toString())
        }


    }

    fun userDataInsert(uid: String, userModel: UserModel) = viewModelScope.launch {
        fireBaseRepository.insertUser(uid, userModel)
    }

    fun emailCheck() = viewModelScope.launch {

        emailList = ArrayList()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    emailList.clear()
                    for (data in dataSnapshot.children) {

                        emailList.add(data.value.toString())
                        Log.d("emailList", emailList.toString())
                    }


                } catch (e: java.lang.Exception) {
                    Log.d("asdf", e.toString())
                }


                _emailCheckResult.value = emailList

            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FireBaseRef.emailCheck.addValueEventListener(postListener)

    }


}


