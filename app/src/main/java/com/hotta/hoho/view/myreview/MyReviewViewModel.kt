package com.hotta.hoho.view.myreview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hotta.hoho.repository.FireBaseRepository
import com.hotta.hoho.repository.NetworkRepository
import com.hotta.hoho.utils.FireBaseAuthUtils
import com.hotta.hoho.utils.FireBaseRef
import com.hotta.hoho.view.detail.ReviewModel
import com.hotta.hoho.view.join.JoinViewModel
import kotlinx.coroutines.launch
import java.lang.Exception


class MyReviewViewModel : ViewModel() {
    private val fireBaseRepository = FireBaseRepository()
    private val TAG = "!!@@" + JoinViewModel::class.java.simpleName

    lateinit var myReviewDataList: ArrayList<MyReviewModel>
    lateinit var myReviewIdList: ArrayList<String>

    private val networkRepository = NetworkRepository()
    var movieLikeReviewMap = HashMap<String, MutableList<String>>()

    private var _reviewLikeData = MutableLiveData<HashMap<String, String>>()
    val reviewLikeData: LiveData<HashMap<String, String>>
        get() = _reviewLikeData

    private var _myReviewIds = MutableLiveData<List<String>>()
    val myReviewIds: LiveData<List<String>>
        get() = _myReviewIds

    private var _myReviewDatas = MutableLiveData<List<MyReviewModel>>()
    val myReviewDatas: LiveData<List<MyReviewModel>>
        get() = _myReviewDatas

    private var _reviewData = MutableLiveData<List<ReviewModel>>()
    val reviewData: LiveData<List<ReviewModel>>
        get() = _reviewData

    fun getMyReviewId() = viewModelScope.launch {
        myReviewIdList = ArrayList()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                myReviewIdList.clear()
                try {
                    for (msg in dataSnapshot.children) {
                        myReviewIdList.add(msg.toString())
                    }
                    //myReviewIdList.reverse()

                } catch (e: Exception) {
                    Log.d(TAG, e.toString())
                }
                Log.d(TAG, myReviewIdList.toString())

                _myReviewIds.value = myReviewIdList

            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FireBaseRef.userReview.child(FireBaseAuthUtils.getUid()).addValueEventListener(postListener)
    }

    fun getMyReviewData() = viewModelScope.launch {
        myReviewDataList = ArrayList()
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                myReviewDataList.clear()
                try {
                    if (snapshot.exists()) {
                        for (movieSnapshot in snapshot.children) {
                            val movieId = movieSnapshot.key

                            if (movieId != null) {
                                val myReviewModel =
                                    movieSnapshot.getValue(MyReviewModel::class.java)
                                if (myReviewModel != null) {
                                    myReviewModel.moveId = movieId;
                                    myReviewDataList.add(myReviewModel)
                                }
                            }
                        }
                    }
                    myReviewDataList.reverse()

                } catch (e: Exception) {
                    Log.d(TAG, e.toString())
                }
                Log.d(TAG, myReviewDataList.toString())

                _myReviewDatas.value = myReviewDataList

            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FireBaseRef.userReview.child(FireBaseAuthUtils.getUid())
            .addValueEventListener(postListener)
    }




    fun selectReviewLikeData(movieId: String) = viewModelScope.launch {
        movieLikeReviewMap = HashMap<String, MutableList<String>>()
        val userIdList = mutableListOf<String>()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
              //  movieLikeReviewMap.clear()
                try {

                    for (data in dataSnapshot.children) {
                        val uid = data.key.toString()
                        userIdList.add(uid)
                        Log.d("asdf", uid)

                    }
                    movieLikeReviewMap[movieId] = userIdList
                } catch (e: Exception) {
                    Log.d("asdf", e.toString())
                }

                Log.d("asdf", movieLikeReviewMap.toString())

        //        _reviewLikeData.value = movieLikeReviewList

            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FireBaseRef.reviewLike.child(movieId).child(FireBaseAuthUtils.getUid())
            .addValueEventListener(postListener)
    }


}

