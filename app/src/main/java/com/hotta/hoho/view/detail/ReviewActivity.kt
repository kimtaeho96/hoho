package com.hotta.hoho.view.detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hotta.hoho.R
import com.hotta.hoho.databinding.ActivityReviewBinding
import com.hotta.hoho.utils.FireBaseAuthUtils
import com.hotta.hoho.utils.FireBaseRef
import java.io.ByteArrayOutputStream

class ReviewActivity : AppCompatActivity() {

    lateinit var binding: ActivityReviewBinding
    private val viewModel: DetailViewModel by viewModels()
    lateinit var getId: String


    lateinit var imgView: ImageView
    private var goodBadCheck: String = ""
    private var uid: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getModify = intent.getStringExtra("수정")
        var hashMap = HashMap<String, Any>()

        //리뷰 수정하는경우
        if (getModify != null) {
            Toast.makeText(this, getModify.toString(), Toast.LENGTH_SHORT).show()
            Log.d("ReviewActivity1", getModify.toString())
            Log.d("ReviewActivity1", FireBaseAuthUtils.getUid().toString())
            viewModel.selectReviewModifyData(getModify, FireBaseAuthUtils.getUid())
            viewModel.modifyData.observe(this, Observer {

                Log.d("ReviewActivity1", it.toString())

                if (it.goodBad == "good") {
                    binding.tvGood.setBackgroundResource(R.drawable.rv_custom_tv_red)
                    binding.tvBad.setBackgroundResource(R.drawable.rv_custom_tv)
                    goodBadCheck = it.goodBad
                } else {
                    binding.tvGood.setBackgroundResource(R.drawable.rv_custom_tv)
                    binding.tvBad.setBackgroundResource(R.drawable.rv_custom_tv_red)
                    goodBadCheck = it.goodBad
                }

                binding.reviewEdit.setText(it.text)

                val storageReference = Firebase.storage.reference
                val mountainsRef =
                    storageReference.child(getModify).child(FireBaseAuthUtils.getUid() + ".png")
                val imageView = binding.rvImgView
                val frameLy = binding.imageFrame
                val imageDel = binding.imageDelete


                mountainsRef.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        frameLy.visibility = View.VISIBLE
                        imageDel.visibility = View.VISIBLE
                        Glide.with(getApplicationContext()).load(task.result).into(imageView)

                    } else {

                    }
                }

            })


        }

        //리뷰를 작성하는 경우
        getId = intent.getStringExtra("id").toString()

        Log.d("ReviewActivity1", getId.toString())
        uid = FireBaseAuthUtils.getUid()

        imgView = binding.rvImgView


        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                imgView.setImageURI(uri)
                binding.imageFrame.visibility = View.VISIBLE
                binding.imageDelete.visibility = View.VISIBLE
            }
        )

        binding.cameraBtn.setOnClickListener {
            getAction.launch("image/*")
        }

        binding.imageDelete.setOnClickListener {
            binding.imageFrame.visibility = View.GONE
            binding.rvImgView.setImageResource(0)
        }

        binding.tvGood.setOnClickListener {
            binding.tvGood.setBackgroundResource(R.drawable.rv_custom_tv_red)
            binding.tvBad.setBackgroundResource(R.drawable.rv_custom_tv)
            goodBadCheck = "good"
            Log.d("ReviewActivity2", goodBadCheck.toString())
        }
        binding.tvBad.setOnClickListener {
            binding.tvGood.setBackgroundResource(R.drawable.rv_custom_tv)
            binding.tvBad.setBackgroundResource(R.drawable.rv_custom_tv_red)
            goodBadCheck = "bad"
            Log.d("ReviewActivity2", goodBadCheck.toString())

        }


        binding.reviewBtn.setOnClickListener {

            /*if(getModify != null){
                Toast.makeText(this, "데이터수정", Toast.LENGTH_SHORT).show()

            }else{*/
            val review = binding.reviewEdit.text.toString()
            Log.d("ReviewActivity3", review.toString())

            if (goodBadCheck == "") {
                Toast.makeText(this, "좋았어요,별로에요를 선택해주세요", Toast.LENGTH_SHORT).show()

            } else {
                if (review == "") {
                    Toast.makeText(this, "리뷰를 작성해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    if (getModify != null) {
                        hashMap.put("goodBad", goodBadCheck)
                        hashMap.put("text", review)
                        hashMap.put("time", "time")

                        FireBaseRef.movieReview.child(getModify).child(FireBaseAuthUtils.getUid())
                            .updateChildren(hashMap)
                        FireBaseRef.userReview.child(FireBaseAuthUtils.getUid()).child(getModify)
                            .updateChildren(hashMap)

                        finish()
                    } else {

                        FireBaseRef.userInfo.child(FireBaseAuthUtils.getUid()).child("name").get()
                            .addOnSuccessListener {

                                Log.i("firebase", "Got value ${it.value}")
                                val name = it.value.toString()
                                val reviewModel =
                                    ReviewModel("time", name, uid, goodBadCheck, review, 0)
                                if (getModify != null) {
                                    getId = getModify
                                }

                                viewModel.reviewDataInsert(getId, reviewModel)


                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }

                        if (imgView.drawable != null) {
                            if (getModify != null) {
                                getId = getModify
                            }
                            imgUpload(getId)
                        }
                        /*  val intent = Intent(this, MovieDetailActivity::class.java)
                      intent.putExtra("id", getId)
                      startActivity(intent)*/
                        finish()
                    }
                }
            }
            /*}*/


        }

    }

    fun imgUpload(getId: String) {
        var storage = Firebase.storage
        val storageRef = storage.reference

        val mountainsRef = storageRef.child(getId).child(uid + ".png")

        imgView.isDrawingCacheEnabled = true
        imgView.buildDrawingCache()
        val bitmap = (imgView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {

        }.addOnSuccessListener { taskSnapshot ->

        }
    }
}