package com.hotta.hoho.view.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hotta.hoho.repository.FireBaseRepository
import com.hotta.hoho.utils.FireBaseAuthUtils
import com.hotta.hoho.utils.FireBaseRef
import com.hotta.hoho.view.adapter.ReviewMovieAdapter
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailViewModel : ViewModel() {
    private val fireBaseRepository = FireBaseRepository()

    lateinit var movieReviewList: ArrayList<ReviewModel>
    lateinit var movieModifyReviewList: ArrayList<ReviewModel>
    var movieLikeReviewList = mutableListOf<String>()


    private var _reviewData = MutableLiveData<List<ReviewModel>>()
    val reviewData: LiveData<List<ReviewModel>>
        get() = _reviewData

    private var _reviewLikeData = MutableLiveData<List<String>>()
    val reviewLikeData: LiveData<List<String>>
        get() = _reviewLikeData

    private var _modifyData = MutableLiveData<ReviewModel>()
    val modifyData: LiveData<ReviewModel>
        get() = _modifyData

    fun reviewDataInsert(id: String, reviewModel: ReviewModel) = viewModelScope.launch {
        fireBaseRepository.insertReview(id, reviewModel)
        fireBaseRepository.insertUserReview(reviewModel,id)
    }

    fun selectReviewData(id: String) = viewModelScope.launch {
        movieReviewList = ArrayList()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                movieReviewList.clear()
                try {
                    for (msg in dataSnapshot.children) {
                        val data = msg.getValue(ReviewModel::class.java)
                        Log.d("asdf", msg.toString())
                        movieReviewList.add(data!!)

                    }
                    movieReviewList.reverse()

                } catch (e: Exception) {
                    Log.d("asdf", e.toString())
                }
                Log.d("asdf", movieReviewList.toString())

                _reviewData.value = movieReviewList

            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FireBaseRef.movieReview.child(id).addValueEventListener(postListener)
    }

    fun selectReviewLikeData() = viewModelScope.launch {
        movieLikeReviewList = ArrayList()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                movieLikeReviewList.clear()
                try {
                    for (data in dataSnapshot.children) {

                        movieLikeReviewList.add(data.key.toString())

                    }
                    movieLikeReviewList.reverse()

                } catch (e: Exception) {
                    Log.d("asdf", e.toString())
                }


                _reviewLikeData.value = movieLikeReviewList

            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FireBaseRef.reviewLike.child(FireBaseAuthUtils.getUid()).addValueEventListener(postListener)
    }

    fun selectReviewModifyData(movieId: String, uid: String) = viewModelScope.launch {
        movieModifyReviewList = ArrayList()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                movieModifyReviewList.clear()
                try {

                    val data = dataSnapshot.getValue(ReviewModel::class.java)


                    _modifyData.value = data!!
                } catch (e: Exception) {
                    Log.d("asdf", e.toString())
                }


            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FireBaseRef.movieReview.child(movieId).child(uid).addValueEventListener(postListener)
    }
}


