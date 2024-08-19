package com.hotta.hoho.view.myreview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hotta.hoho.R
import com.hotta.hoho.databinding.ActivityJoinBinding
import com.hotta.hoho.databinding.ActivityMyReviewBinding
import com.hotta.hoho.utils.FireBaseAuthUtils
import com.hotta.hoho.utils.FireBaseRef
import com.hotta.hoho.view.adapter.MapAdapter
import com.hotta.hoho.view.adapter.MyReviewAdapter
import com.hotta.hoho.view.custom.CustomPopup
import com.hotta.hoho.view.detail.DetailViewModel
import com.hotta.hoho.view.detail.ReviewActivity
import com.hotta.hoho.view.detail.ReviewModel
import com.hotta.hoho.view.join.JoinViewModel

class MyReviewActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyReviewBinding
    private val viewModel: MyReviewViewModel by viewModels()
    private val TAG = "!!@@" + MyReviewActivity::class.java.simpleName
    lateinit var adapter: MyReviewAdapter
    var allMoveIdList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.getMyReviewData()

        viewModel.reviewLikeData.observe(this) {
            Log.d(TAG, "review Data : $it")

        }


        viewModel.myReviewDatas.observe(this) {

            if(it.isNotEmpty()){
                binding.myReviewRv.visibility=View.VISIBLE
                binding.noReviewText.visibility=View.GONE
            }else{
                binding.myReviewRv.visibility=View.GONE
                binding.noReviewText.visibility=View.VISIBLE
            }

            for (item in it) {
                allMoveIdList.add(item.moveId)
            }
            for (moveId in allMoveIdList) {
                viewModel.selectReviewLikeData(moveId)
            }

            adapter = MyReviewAdapter(this, it)

            binding.myReviewRv.adapter = adapter
            binding.myReviewRv.layoutManager = LinearLayoutManager(this)

            adapter.itemClick = object : MyReviewAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {


                    val modify = view.findViewById<TextView>(R.id.my_review_movie_modify)
                    val delete = view.findViewById<TextView>(R.id.my_review_movie_delete)

                    val moveId = it.get(position).moveId
                    modify.setOnClickListener {
                        Toast.makeText(this@MyReviewActivity, "수정 클릭", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@MyReviewActivity, ReviewActivity::class.java)
                        intent.putExtra("수정", moveId)
                        startActivity(intent)
                    }

                    delete.setOnClickListener {
                        val popup = CustomPopup(this@MyReviewActivity, object : CustomPopup.PopupListener {
                            override fun conFirmClick() {
                                var storage = Firebase.storage
                                val storageRef = storage.reference
                                Toast.makeText(baseContext, "삭제 클릭.", Toast.LENGTH_SHORT).show()

                                FireBaseRef.movieReview.child(moveId)
                                    .child(FireBaseAuthUtils.getUid())
                                    .removeValue()

                                FireBaseRef.userReview.child(FireBaseAuthUtils.getUid())
                                    .child(moveId)
                                    .removeValue()

                                FireBaseRef.reviewLike.child(moveId).child(FireBaseAuthUtils.getUid())
                                    .removeValue()

                                FireBaseRef.myReviewLike.child(moveId).child(FireBaseAuthUtils.getUid())
                                    .removeValue()

                                val mountainsRef =
                                    storageRef.child(moveId)
                                        .child(FireBaseAuthUtils.getUid() + ".png")
                                mountainsRef.delete().addOnSuccessListener {
                                }.addOnFailureListener {
                                }
                            }

                            override fun cancelClick() {

                            }
                        }, "리뷰 삭제", "리뷰를 삭제 하시겠습니까?", true)
                        popup.setCancelable(false)
                        popup.show()


                    }


                }

            }
        }


    }
}