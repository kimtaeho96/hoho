package com.hotta.hoho.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hotta.hoho.R
import com.hotta.hoho.utils.FireBaseAuthUtils
import com.hotta.hoho.utils.FireBaseRef
import com.hotta.hoho.view.detail.ReviewLkeModel
import com.hotta.hoho.view.detail.ReviewModel
import java.util.Objects

class ReviewMovieAdapter(
    val context: Context,
    val item: List<ReviewModel>,
    val movieId: String,
    val key: List<String>,
    val reviewLikeList: List<String>
) :
    RecyclerView.Adapter<ReviewMovieAdapter.ViewHolder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val name = itemView.findViewById<TextView>(R.id.name)
        val time = itemView.findViewById<TextView>(R.id.time)
        val text = itemView.findViewById<TextView>(R.id.text)
        val likeImg = itemView.findViewById<ImageView>(R.id.reviewLike)
        val deleteLayout = itemView.findViewById<LinearLayout>(R.id.deletelayout)
        val goodbad = itemView.findViewById<TextView>(R.id.goodbad)
        val likecount = itemView.findViewById<TextView>(R.id.likeCount)


        fun bind(item: ReviewModel, key: String) {
            likeImg.setImageResource(R.drawable.like_grey)


            if (reviewLikeList.contains(key)) {
                likeImg.setImageResource(R.drawable.like_red)
            } else {
                likeImg.setImageResource(R.drawable.like_grey)
            }

            if (item.userid.equals(FireBaseAuthUtils.getUid())) {
                deleteLayout.visibility = View.VISIBLE
            }

            likecount.text = item.count.toString()
            time.text = item.time
            name.text = item.name
            text.text = item.text
            if (item.goodBad == "good") {
                goodbad.text = "좋았어요!"
            } else {
                goodbad.text = "별로예요."
            }

            val storageReference = Firebase.storage.reference
            val mountainsRef = storageReference.child(movieId).child(item.userid + ".png")
            val imageView = itemView.findViewById<ImageView>(R.id.reviewImg)

            var count = item.count
            var hashMap = HashMap<String, Any>()

            mountainsRef.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imageView.visibility = View.VISIBLE
                    Glide.with(context /* context */).load(task.result).into(imageView)
                } else {
                    imageView.visibility = View.GONE
                }
            }

            likeImg.setOnClickListener {
                if (reviewLikeList.contains(key)) {
                    count--

                    if (count >= 0) {
                        hashMap.put("count", count)


                        FireBaseRef.movieReview.child(movieId).child(item.userid)
                            .updateChildren(hashMap)
                        FireBaseRef.userReview.child(item.userid).child(movieId)
                            .updateChildren(hashMap)
                    }

                    FireBaseRef.reviewLike.child(FireBaseAuthUtils.getUid()).child(key)
                        .removeValue()


                } else {
                    count++

                    hashMap.put("count", count)


                    FireBaseRef.movieReview.child(movieId).child(item.userid)
                        .updateChildren(hashMap as Map<String, Any>)
                    FireBaseRef.userReview.child(item.userid).child(movieId)
                        .updateChildren(hashMap as Map<String, Any>)

                    FireBaseRef.reviewLike.child(FireBaseAuthUtils.getUid()).child(key)
                        .setValue(ReviewLkeModel(true))
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.review_movie_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position], key[position])
//클릭이벤트
        if (itemClick != null) {
            holder.itemView.setOnClickListener { v ->
                itemClick?.onClick(v, position)
            }
        }


    }


}