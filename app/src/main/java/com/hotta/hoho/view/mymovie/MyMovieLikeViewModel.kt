package com.hotta.hoho.view.mymovie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hotta.hoho.network.model.DetailMovieResponse
import com.hotta.hoho.repository.FireBaseRepository
import com.hotta.hoho.repository.NetworkRepository
import com.hotta.hoho.utils.FireBaseAuthUtils
import com.hotta.hoho.utils.FireBaseRef
import com.hotta.hoho.view.detail.ReviewModel
import com.hotta.hoho.view.join.JoinViewModel
import kotlinx.coroutines.launch
import java.lang.Exception


class MyMovieLikeViewModel : ViewModel() {
    private val fireBaseRepository = FireBaseRepository()
    private val TAG = "!!@@" + JoinViewModel::class.java.simpleName
    lateinit var myLikeMovieIdList: ArrayList<String>
    lateinit var myMovieDetail: DetailMovieResponse


    private val networkRepository = NetworkRepository()


    private var _myLikeMovieIds = MutableLiveData<List<String>>()
    val myMovieLikeIds: LiveData<List<String>>
        get() = _myLikeMovieIds

    private val _detailMovieResult = MutableLiveData<DetailMovieResponse>()
    val detailMovieResult: LiveData<DetailMovieResponse>
        get() = _detailMovieResult

    fun getMyLikeMovieId() = viewModelScope.launch {
        myLikeMovieIdList = ArrayList()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                myLikeMovieIdList.clear()
                try {
                    for (data in dataSnapshot.children) {
                        myLikeMovieIdList.add(data.value.toString())
                    }
                } catch (e: Exception) {
                    Log.d(TAG, e.toString())
                }
                Log.d(TAG, myLikeMovieIdList.toString())

                _myLikeMovieIds.value = myLikeMovieIdList

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FireBaseRef.movieLike.child(FireBaseAuthUtils.getUid())
            .addValueEventListener(postListener)
    }

    fun getDetailMovie(id: Int) = viewModelScope.launch {
        try {
            val result = networkRepository.getDeltailMovie(id, "8f20c3de95e081c58a1a1ca38e4f7d73")
            Log.d("MainViewModel(Detail)", result.toString())

            _detailMovieResult.value = result

        } catch (e: Exception) {
            Log.d("MainViewModel(Detail)", e.toString())
        }

    }
}


